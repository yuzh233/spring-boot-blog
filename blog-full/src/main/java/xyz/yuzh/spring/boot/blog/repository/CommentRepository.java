package xyz.yuzh.spring.boot.blog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import xyz.yuzh.spring.boot.blog.domain.Comment;

/**
 * @author yu.zh [yuzh233@gmail.com]
 * @date 2018/10/26
 */
public interface CommentRepository extends JpaRepository<Comment, Long> {

}
