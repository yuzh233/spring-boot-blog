package xyz.yuzh.spring.boot.blog.blogprototype.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 主页控制器.
 */
@Controller
@RequestMapping("/blogs")
public class BlogController {

    /**
     *
     * @param order 默认根据最新博客排序
     * @param keyword 关键字
     * @return
     */
    @GetMapping
    public String listBlogs(@RequestParam(value = "order", required = false, defaultValue = "new") String order,
                            @RequestParam(value = "keyword", required = false) Long keyword) {
        System.out.print("order:" + order + ";keyword:" + keyword);
        return "redirect:/index?order=" + order + "&keyword=" + keyword;
    }

}
