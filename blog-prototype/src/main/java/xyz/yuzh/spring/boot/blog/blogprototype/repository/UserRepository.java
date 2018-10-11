package xyz.yuzh.spring.boot.blog.blogprototype.repository;

import org.springframework.data.repository.CrudRepository;
import xyz.yuzh.spring.boot.blog.blogprototype.entity.User;

/**
 * @author yu.zh [yuzh233@gmail.com] 2018/09/25 21:57
 * <p>
 * CrudRepository 提供了常用接口，自己无需编写接口。
 */
public interface UserRepository extends CrudRepository<User, Long> {

}
