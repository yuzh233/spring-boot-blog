package xyz.yuzh.spring.boot.blog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import xyz.yuzh.spring.boot.blog.domain.User;
import xyz.yuzh.spring.boot.blog.service.BlogService;
import xyz.yuzh.spring.boot.blog.service.VoteService;
import xyz.yuzh.spring.boot.blog.vo.Response;

/**
 * 用户点赞控制器
 *
 * @author yu.zh [yuzh233@gmail.com]
 * @date 2018/10/26
 */
@Controller
@RequestMapping("/votes")
public class VoteController {

    @Autowired
    private BlogService blogService;

    @Autowired
    private VoteService voteService;

    /**
     * 发表点赞
     */
    @PostMapping
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_USER')")
    public ResponseEntity<Response> createVote(Long blogId) {

        blogService.createVote(blogId);

        return ResponseEntity.ok().body(new Response(true, "点赞成功", null));
    }

    /**
     * 删除点赞
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_USER')")
    public ResponseEntity<Response> delete(@PathVariable("id") Long id, Long blogId) {

        boolean isOwner = false;
        User user = voteService.getVoteById(id).getUser();

        // 判断操作用户是否是点赞的所有者
        if (SecurityContextHolder.getContext().getAuthentication() != null && SecurityContextHolder.getContext().getAuthentication().isAuthenticated()
                && !SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString().equals("anonymousUser")) {
            User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (principal != null && user.getUsername().equals(principal.getUsername())) {
                isOwner = true;
            }
        }
        if (!isOwner) {
            return ResponseEntity.ok().body(new Response(false, "没有操作权限"));
        }

        // 博客关联表中删除指定点赞
        blogService.removeVote(blogId, id);
        // 点赞表中删除指定点赞
        voteService.removeVote(id);

        return ResponseEntity.ok().body(new Response(true, "取消点赞成功", null));
    }
}
