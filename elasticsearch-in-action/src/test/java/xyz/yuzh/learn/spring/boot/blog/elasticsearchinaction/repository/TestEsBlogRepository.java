package xyz.yuzh.learn.spring.boot.blog.elasticsearchinaction.repository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;
import xyz.yuzh.learn.spring.boot.blog.elasticsearchinaction.entity.EsBlog;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestEsBlogRepository {

    @Autowired
    private EsBlogRepository esBlogRepository;

    /**
     * 测试文档库之前存入数据
     */
    @Before
    public void initEsBlogRepository() {
        esBlogRepository.deleteAll();
        esBlogRepository.save(new EsBlog("登鹤雀楼", "王之涣的登鹤雀楼",
                "白日依山尽，黄河入海流。欲穷千里目，更上一层楼。"));
        esBlogRepository.save(new EsBlog("相思", "王维的相思",
                "红豆生南国，春来发几枝。愿君多采颉，此物最相思。"));
        esBlogRepository.save(new EsBlog("静夜思", "李白的静夜思",
                "床前明月光，疑是地上霜。举头望明月，低头思故乡。"));
    }

    @Test
    public void testFindDistinctEsBlogByTitleContainingOrSummaryContainingOrContentContaining() {
        Pageable pageRequest = new PageRequest(0, 20);
        String title = "思";
        String summary = "思";
        String content = "相思";
        Page<EsBlog> page = esBlogRepository.findDistinctEsBlogByTitleContainingOrSummaryContainingOrContentContaining(
                title, summary, content, pageRequest);
        // 断言根据指定的 title、summary、content 来搜索 记录有两条
        assertThat(page.getTotalElements()).isEqualTo(2);

        // 打印结果
        for (EsBlog blog : page.getContent()){
            System.out.println(blog);
        }
    }

}
