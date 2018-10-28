package xyz.yuzh.spring.boot.blog.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import xyz.yuzh.spring.boot.blog.domain.Blog;
import xyz.yuzh.spring.boot.blog.domain.Catalog;
import xyz.yuzh.spring.boot.blog.domain.User;

/**
 * @author yu.zh [yuzh233@gmail.com]
 * @date 2018/10/25
 */
public interface BlogRepository extends JpaRepository<Blog, Long> {

    /**
     * 根据用户名、博客标题分页查询博客列表（时间逆序）
     */
    Page<Blog> findByUserAndTitleLikeOrderByCreateTimeDesc(User user, String title, Pageable pageable);

    /**
     * 根据用户名、博客标题分页查询博客列表
     */
    Page<Blog> findByUserAndTitleLike(User user, String title, Pageable pageable);

    /**
     * 根据指定用户和模糊标题 or 根据模糊标签和指定用户
     * 分页查询博客列表（时间逆序）
     *
     * @param title    根据博客标题模糊查询
     * @param user     根据博客标题模糊时的指定用户
     * @param tags     根据标签查询
     * @param user2    根据标签模糊查询时的指定用户
     * @param pageable 分页参数
     */
    Page<Blog> findByTitleLikeAndUserOrTagsLikeAndUserOrderByCreateTimeDesc(String title, User user, String tags, User user2, Pageable pageable);

    /**
     * 根据分类 分页查询博客列表
     */
    Page<Blog> findByCatalog(Catalog catalog, Pageable pageable);
}
