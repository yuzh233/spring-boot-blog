package xyz.yuzh.spring.boot.blog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.yuzh.spring.boot.blog.domain.Vote;
import xyz.yuzh.spring.boot.blog.repository.VoteRepository;

/**
 * @author yu.zh [yuzh233@gmail.com]
 * @date 2018/10/26
 */
@Service
public class VoteServiceImpl implements VoteService {

    @Autowired
    private VoteRepository voteRepository;

    @Override
    public Vote getVoteById(Long id) {
        return voteRepository.findOne(id);
    }

    @Override
    public void removeVote(Long id) {
        voteRepository.delete(id);
    }
}
