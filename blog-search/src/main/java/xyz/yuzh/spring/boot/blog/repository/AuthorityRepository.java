package xyz.yuzh.spring.boot.blog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import xyz.yuzh.spring.boot.blog.domain.Authority;

/**
 * 角色资源库
 *
 * @author yu.zh [yuzh233@gmail.com]
 * @date 2018/10/14
 */
public interface AuthorityRepository extends JpaRepository<Authority, Long> {

    Authority findAuthorityById(Long id);
}
