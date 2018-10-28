package xyz.yuzh.spring.boot.blog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.yuzh.spring.boot.blog.domain.Comment;
import xyz.yuzh.spring.boot.blog.repository.CommentRepository;

/**
 * @author yu.zh [yuzh233@gmail.com]
 * @date 2018/10/26
 */
@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Override
    public Comment getCommentById(Long id) {
        return commentRepository.getOne(id);
    }

    @Override
    public void removeComment(Long id) {
        commentRepository.delete(id);
    }
}
