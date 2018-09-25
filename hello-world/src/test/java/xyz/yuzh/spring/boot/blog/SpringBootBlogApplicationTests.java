package xyz.yuzh.spring.boot.blog;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class SpringBootBlogApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testHello() throws Exception {
        ResultActions actions = mockMvc.perform(MockMvcRequestBuilders.get("/hello").
                accept(MediaType.APPLICATION_JSON)); // 指定客户端能够接收的内容类型

        actions.andExpect(status().isOk()); // 添加断言, 添加ResultMatcher验证规则，验证控制器执行完成后结果是否正确.
        actions.andExpect(content().string(equalTo("hello gradle!"))); // 添加断言,返回结果内容是否是指定的.
        actions.andDo(MockMvcResultHandlers.print()); // 添加一个结果处理器，输出整个响应结果信息.
        actions.andReturn(); // 执行完毕返回相应的结果
    }
}
