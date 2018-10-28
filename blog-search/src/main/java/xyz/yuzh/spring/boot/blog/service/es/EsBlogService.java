package xyz.yuzh.spring.boot.blog.service.es;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import xyz.yuzh.spring.boot.blog.domain.User;
import xyz.yuzh.spring.boot.blog.domain.es.EsBlog;
import xyz.yuzh.spring.boot.blog.vo.TagVO;

import java.util.List;

/**
 * @author yu.zh [yuzh233@gmail.com]
 * @date 2018/10/27
 */
public interface EsBlogService {

    /**
     * 删除Blog
     */
    void removeEsBlog(String id);

    /**
     * 更新 EsBlog
     */
    EsBlog updateEsBlog(EsBlog esBlog);

    /**
     * 根据 id 获取 Blog
     */
    EsBlog getEsBlogByBlogId(Long blogId);

    /**
     * 最新博客列表，分页
     */
    Page<EsBlog> listNewestEsBlogs(String keyword, Pageable pageable);

    /**
     * 最热博客列表，分页
     */
    Page<EsBlog> listHotestEsBlogs(String keyword, Pageable pageable);

    /**
     * 博客列表，分页
     */
    Page<EsBlog> listEsBlogs(Pageable pageable);

    /**
     * 最新前5
     */
    List<EsBlog> listTop5NewestEsBlogs();

    /**
     * 最热前5
     */
    List<EsBlog> listTop5HotestEsBlogs();

    /**
     * 最热前 30 标签
     */
    List<TagVO> listTop30Tags();

    /**
     * 最热前12用户
     */
    List<User> listTop12Users();
}
