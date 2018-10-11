package xyz.yuzh.learn.spring.boot.blog.elasticsearchinaction.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import xyz.yuzh.learn.spring.boot.blog.elasticsearchinaction.entity.EsBlog;
import xyz.yuzh.learn.spring.boot.blog.elasticsearchinaction.repository.EsBlogRepository;

import java.util.List;

@RestController
@RequestMapping("/")
public class BlogController {

    @Autowired
    private EsBlogRepository esBlogRepository;

    @RequestMapping(value = "/blogs")
    public List<EsBlog> list(@RequestParam(value = "title") String title,
                             @RequestParam(value = "summary") String summary,
                             @RequestParam(value = "content") String content,
                             @RequestParam(value = "pageIndex", defaultValue = "0") int pageIndex,
                             @RequestParam(value = "pageSize", defaultValue = "10") int pageSize
    ) {
        Pageable pageable = new PageRequest(0, 10);
        Page<EsBlog> page = esBlogRepository.findDistinctEsBlogByTitleContainingOrSummaryContainingOrContentContaining(
                title, summary, content, pageable);

        return page.getContent();
    }
}
