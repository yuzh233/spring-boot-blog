package xyz.yuzh.spring.boot.blog;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import xyz.yuzh.spring.boot.blog.entity.User;
import xyz.yuzh.spring.boot.blog.repository.UserRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BlogUserApplicationTests {

    @Autowired
    UserRepository userRepository;

    @Test
    public void contextLoads() {
        User exists = userRepository.queryUserByUsername("yuzh");
        System.out.println(exists);

        User email = userRepository.queryUserByEmail("yuzh233@163.com");
        System.out.println(email);
    }

}
