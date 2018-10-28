package xyz.yuzh.spring.boot.blog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import xyz.yuzh.spring.boot.blog.domain.*;
import xyz.yuzh.spring.boot.blog.domain.es.EsBlog;
import xyz.yuzh.spring.boot.blog.repository.BlogRepository;
import xyz.yuzh.spring.boot.blog.service.es.EsBlogService;

import javax.transaction.Transactional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author yu.zh [yuzh233@gmail.com]
 * @date 2018/10/25
 */
@Service
public class BlogServiceImpl implements BlogService {

    @Autowired
    private BlogRepository blogRepository;

    @Autowired
    private EsBlogService esBlogService;

    @Transactional
    @Override
    public Blog saveBlog(Blog blog) {
        boolean isNew = (blog.getId() == null);
        EsBlog esBlog = null;

        Blog returnBlog = blogRepository.save(blog);

        if (isNew) {
            esBlog = new EsBlog(returnBlog);
        } else {
            esBlog = esBlogService.getEsBlogByBlogId(blog.getId());
            esBlog.update(returnBlog);
        }
        // 存入数据库的同时还需要存入全文索引库
        esBlogService.updateEsBlog(esBlog);
        return returnBlog;
    }

    @Transactional
    @Override
    public void removeBlog(Long id) {
        blogRepository.delete(id);
        EsBlog esblog = esBlogService.getEsBlogByBlogId(id);
        // 在数据库删除的同时还需要在索引库中删除
        esBlogService.removeEsBlog(esblog.getId());
    }

    @Transactional
    @Override
    public Blog updateBlog(Blog blog) {
        Blog originBlog = blogRepository.save(blog);
        EsBlog esblog = esBlogService.getEsBlogByBlogId(blog.getId());
        esblog.update(originBlog);
        esBlogService.updateEsBlog(esblog);
        return originBlog;
    }

    @Override
    public Blog getBlogById(Long id) {
        return blogRepository.findOne(id);
    }

    /**
     * 根据用户名进行分页模糊查询（最新）
     */
    @Override
    public Page<Blog> listBlogsByTitleLike(User user, String title, Pageable pageable) {
        // 模糊查询
        title = "%" + title + "%";
        String tags = title;
//        Page<Blog> blogs = blogRepository.findByUserAndTitleLikeOrderByCreateTimeDesc(user, title, pageable);
        Page<Blog> blogs = blogRepository.findByTitleLikeAndUserOrTagsLikeAndUserOrderByCreateTimeDesc(title, user, tags, user, pageable);
        return blogs;
    }

    /**
     * 根据用户名进行分页模糊查询（最热）
     */
    @Override
    public Page<Blog> listBlogsByTitleLikeAndSort(User user, String title, Pageable pageable) {
        // 模糊查询
        title = "%" + title + "%";
        Page<Blog> blogs = blogRepository.findByUserAndTitleLike(user, title, pageable);
        return blogs;
    }

    @Override
    public void readingIncrease(Long id) {
        Blog blog = blogRepository.findOne(id);
        blog.setReadSize(blog.getReadSize() + 1);
        blogRepository.save(blog);
    }

    /******** 评论管理 *********/

    @Override
    public Blog createComment(Long blogId, String commentContent) {
        Blog originalBlog = blogRepository.findOne(blogId);
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Comment comment = new Comment(user, commentContent);
        originalBlog.addComment(comment);

        // 更新索引库博客
        EsBlog esblog = esBlogService.getEsBlogByBlogId(originalBlog.getId());
        esblog.update(originalBlog);
        esBlogService.updateEsBlog(esblog);

        return blogRepository.save(originalBlog);
    }

    @Override
    public void removeComment(Long blogId, Long commentId) {
        Blog originalBlog = blogRepository.findOne(blogId);
        originalBlog.removeComment(commentId);

        // 更新索引库博客
        EsBlog esblog = esBlogService.getEsBlogByBlogId(originalBlog.getId());
        esblog.update(originalBlog);
        esBlogService.updateEsBlog(esblog);

        blogRepository.save(originalBlog);
    }

    /******** 点赞管理 *********/

    @Override
    public Blog createVote(Long blogId) {
        Blog originalBlog = blogRepository.findOne(blogId);
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Vote vote = new Vote(user);
        boolean isExist = originalBlog.addVote(vote);
        if (isExist) {
            throw new IllegalArgumentException("已顶~");
        }

        // 更新索引库博客
        EsBlog esblog = esBlogService.getEsBlogByBlogId(originalBlog.getId());
        esblog.update(originalBlog);
        esBlogService.updateEsBlog(esblog);

        return blogRepository.save(originalBlog);
    }

    @Override
    public void removeVote(Long blogId, Long voteId) {
        Blog originalBlog = blogRepository.findOne(blogId);
        originalBlog.removeVote(voteId);

        // 更新索引库博客
        EsBlog esblog = esBlogService.getEsBlogByBlogId(originalBlog.getId());
        esblog.update(originalBlog);
        esBlogService.updateEsBlog(esblog);

        blogRepository.save(originalBlog);
    }

    /******** 分类管理 *********/

    @Override
    public Page<Blog> listBlogsByCatalog(Catalog catalog, Pageable pageable) {
        Page<Blog> blogs = blogRepository.findByCatalog(catalog, pageable);
        return blogs;
    }

    /******** 首页搜索管理 *********/

    @Override
    public void flushElasticSearchBlogsData(Blog originalBlog) {
        ExecutorService pool = Executors.newFixedThreadPool(5);
        EsBlog esBlog = esBlogService.getEsBlogByBlogId(originalBlog.getId());
        pool.submit(new BlogThread(originalBlog, esBlog));
        pool.shutdown();
    }

    /**
     * 同步线程
     */
    private class BlogThread implements Runnable {
        private EsBlog esBlog;
        private Blog originalBlog;

        protected BlogThread(Blog originalBlog, EsBlog esBlog) {
            this.esBlog = esBlog;
            this.originalBlog = originalBlog;
        }

        @Override
        public void run() {
            // 更新索引库博客
            esBlog = esBlogService.getEsBlogByBlogId(originalBlog.getId());
            esBlog.update(originalBlog);
            esBlogService.updateEsBlog(esBlog);
        }
    }
}