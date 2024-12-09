package cor.chrissy.community.service.comment.service.impl;

import cor.chrissy.community.service.comment.repository.mapper.CommentMapper;
import cor.chrissy.community.service.comment.service.CommentService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author wx128
 * @date 2024/12/9
 */
@Service
public class CommentServiceImpl implements CommentService {
    @Resource
    private CommentMapper commentMapper;
}
