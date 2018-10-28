package xyz.yuzh.spring.boot.blog.service;

import xyz.yuzh.spring.boot.blog.domain.Comment;

/**
 * @author yu.zh [yuzh233@gmail.com]
 * @date 2018/10/26
 */
public interface CommentService {

    /**
     * 根据 id 获取 Comment
     */
    Comment getCommentById(Long id);

    /**
     * 删除评论
     */
    void removeComment(Long id);
}
