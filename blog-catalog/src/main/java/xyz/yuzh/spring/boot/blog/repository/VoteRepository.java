package xyz.yuzh.spring.boot.blog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import xyz.yuzh.spring.boot.blog.domain.Vote;

/**
 * @author yu.zh [yuzh233@gmail.com]
 * @date 2018/10/26
 */
public interface VoteRepository extends JpaRepository<Vote, Long> {

}