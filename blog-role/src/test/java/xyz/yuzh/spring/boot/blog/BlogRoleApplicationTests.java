package xyz.yuzh.spring.boot.blog;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import xyz.yuzh.spring.boot.blog.domain.User;
import xyz.yuzh.spring.boot.blog.repository.UserRepository;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BlogRoleApplicationTests {

    @Autowired
    UserRepository userRepository;

    @Test
    public void contextLoads() {
        List<User> list = userRepository.findAll();
        list.stream().forEach(System.out::print);

        boolean exists = userRepository.existsByUsername("yuzh233");
        System.out.println("exists ? " + exists);
    }

}
