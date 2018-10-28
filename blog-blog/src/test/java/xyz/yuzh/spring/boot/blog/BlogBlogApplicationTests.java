package xyz.yuzh.spring.boot.blog;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;
import xyz.yuzh.spring.boot.blog.domain.Blog;
import xyz.yuzh.spring.boot.blog.domain.User;
import xyz.yuzh.spring.boot.blog.repository.BlogRepository;
import xyz.yuzh.spring.boot.blog.service.BlogService;
import xyz.yuzh.spring.boot.blog.service.BlogServiceImpl;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BlogBlogApplicationTests {

    @Autowired
    BlogRepository blogRepository;

    @Test
    public void contextLoads() {
        User user = new User(null);
        user.setUsername("yuzh");
        user.setId(2L);
        String title = "%test%";
        Pageable pageable = new PageRequest(0, 10);
        Page<Blog> blogs = blogRepository.findByUserAndTitleLikeOrderByCreateTimeDesc(user, title, pageable);
        System.out.println("count: "+blogs.getTotalElements());
        List<Blog> blogList = blogs.getContent();

        blogList.stream().forEach(System.out::println);
    }

}
