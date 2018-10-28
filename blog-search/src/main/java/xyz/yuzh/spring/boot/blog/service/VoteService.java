package xyz.yuzh.spring.boot.blog.service;

import xyz.yuzh.spring.boot.blog.domain.Vote;

/**
 * @author yu.zh [yuzh233@gmail.com]
 * @date 2018/10/26
 */
public interface VoteService {

    /**
     * 根据 id 获取 Vote
     */
    Vote getVoteById(Long id);

    /**
     * 删除 Vote
     */
    void removeVote(Long id);
}
