package xyz.yuzh.learn.spring.boot.blog.elasticsearchinaction.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import xyz.yuzh.learn.spring.boot.blog.elasticsearchinaction.entity.EsBlog;

public interface EsBlogRepository extends ElasticsearchRepository<EsBlog, String> {

    /**
     * 分页查询博客（去重）
     * JPA 自动根据方法名执行查询
     */
    Page<EsBlog> findDistinctEsBlogByTitleContainingOrSummaryContainingOrContentContaining(String title, String summary,
                                                                                           String content,
                                                                                           Pageable pageable);


}
