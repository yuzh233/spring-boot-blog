package xyz.yuzh.spring.boot.blog.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import xyz.yuzh.spring.boot.blog.domain.Blog;
import xyz.yuzh.spring.boot.blog.domain.User;

/**
 * @author yu.zh [yuzh233@gmail.com]
 * @date 2018/10/25
 */
public interface BlogRepository extends JpaRepository<Blog, Long> {

    /**
     * 根据用户名、博客标题分页查询用户列表（时间逆序）
     */
    Page<Blog> findByUserAndTitleLikeOrderByCreateTimeDesc(User user, String title, Pageable pageable);

    /**
     * 根据用户名、博客标题分页查询用户列表
     */
    Page<Blog> findByUserAndTitleLike(User user, String title, Pageable pageable);


}
