package xyz.yuzh.spring.boot.blog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import xyz.yuzh.spring.boot.blog.domain.Catalog;
import xyz.yuzh.spring.boot.blog.domain.User;

import java.util.List;

/**
 * @author yu.zh [yuzh233@gmail.com]
 * @date 2018/10/26
 */
public interface CatalogRepository extends JpaRepository<Catalog, Long> {

    /**
     * 根据用户查询
     */
    List<Catalog> findByUser(User user);

    /**
     * 根据用户、分类名查询
     */
    List<Catalog> findByUserAndName(User user, String name);
}
