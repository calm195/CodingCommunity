package cor.chrissy.community.service.comment.service.impl;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import cor.chrissy.community.common.context.ReqInfoContext;
import cor.chrissy.community.common.entity.BaseUserInfoDTO;
import cor.chrissy.community.common.enums.DocumentTypeEnum;
import cor.chrissy.community.common.enums.PraiseStatEnum;
import cor.chrissy.community.common.req.PageParam;
import cor.chrissy.community.service.article.service.CountService;
import cor.chrissy.community.service.comment.converter.CommentConverter;
import cor.chrissy.community.service.comment.dto.BaseCommentDTO;
import cor.chrissy.community.service.comment.dto.SubCommentDTO;
import cor.chrissy.community.service.comment.dto.TopCommentDTO;
import cor.chrissy.community.service.comment.repository.dao.CommentDao;
import cor.chrissy.community.service.comment.repository.entity.CommentDO;
import cor.chrissy.community.service.comment.service.CommentReadService;
import cor.chrissy.community.service.user.repository.entity.UserFootDO;
import cor.chrissy.community.service.user.service.UserFootService;
import cor.chrissy.community.service.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author wx128
 * @createAt 2024/12/18
 */
@Service
public class CommentReadServiceImpl implements CommentReadService {

    @Autowired
    private CommentDao commentDao;

    @Autowired
    private UserService userService;

    @Autowired
    private CountService countService;

    @Autowired
    private UserFootService userFootService;

    @Override
    public CommentDO queryComment(Long commentId) {
        return commentDao.getById(commentId);
    }

    @Override
    public List<TopCommentDTO> getArticleComments(Long articleId, PageParam page) {
        // 1.查询一级评论
        List<CommentDO> comments = commentDao.listTopCommentList(articleId, page);
        if (CollectionUtils.isEmpty(comments)) {
            return Collections.emptyList();
        }
        // map 存 commentId -> 评论
        Map<Long, TopCommentDTO> topComments = comments.stream().collect(Collectors.toMap(CommentDO::getId, CommentConverter::toTopDto));

        // 2.查询非一级评论
        List<CommentDO> subComments = commentDao.listSubCommentIdMappers(articleId, topComments.keySet());

        // 3.构建一级评论的子评论
        buildCommentRelation(subComments, topComments);

        // 4.挑出需要返回的数据，排序，并补齐对应的用户信息，最后排序返回
        List<TopCommentDTO> result = new ArrayList<>();
        comments.forEach(comment -> {
            TopCommentDTO dto = topComments.get(comment.getId());
            fillTopCommentInfo(dto);
            result.add(dto);
        });

        // 返回结果根据时间进行排序
        Collections.sort(result);
        return result;
    }

    /**
     * 构建父子评论关系
     */
    private void buildCommentRelation(List<CommentDO> subComments, Map<Long, TopCommentDTO> topComments) {
        Map<Long, SubCommentDTO> subCommentMap = subComments.stream().collect(Collectors.toMap(CommentDO::getId, CommentConverter::toSubDto));
        subComments.forEach(comment -> {
            TopCommentDTO top = topComments.get(comment.getTopCommentId());
            if (top == null) {
                return;
            }
            SubCommentDTO sub = subCommentMap.get(comment.getId());
            top.getChildComments().add(sub);
            if (Objects.equals(comment.getTopCommentId(), comment.getParentCommentId())) {
                return;
            }

            SubCommentDTO parent = subCommentMap.get(comment.getParentCommentId());
            sub.setParentContent(parent == null ? "~~已删除~~" : parent.getCommentContent());
        });
    }

    /**
     * 填充评论对应的信息
     *
     * @param comment
     */
    private void fillTopCommentInfo(TopCommentDTO comment) {
        fillCommentInfo(comment);
        comment.getChildComments().forEach(this::fillCommentInfo);
        Collections.sort(comment.getChildComments());
    }

    /**
     * 填充评论对应的信息，如用户信息，点赞数等
     *
     * @param comment
     */
    private void fillCommentInfo(BaseCommentDTO comment) {
        BaseUserInfoDTO userInfoDO = userService.queryBasicUserInfo(comment.getUserId());
        if (userInfoDO == null) {
            // 如果用户注销，给一个默认的用户
            comment.setUserName("默认用户");
            comment.setUserPhoto("");
            if (comment instanceof TopCommentDTO) {
                ((TopCommentDTO) comment).setCommentCount(0);
            }
        } else {
            comment.setUserName(userInfoDO.getUserName());
            comment.setUserPhoto(userInfoDO.getPhoto());
            if (comment instanceof TopCommentDTO) {
                ((TopCommentDTO) comment).setCommentCount(((TopCommentDTO) comment).getChildComments().size());
            }
        }

        // 查询点赞数
        Long praiseCount = countService.queryCommentPraiseCount(comment.getCommentId());
        comment.setPraiseCount(praiseCount.intValue());

        // 查询当前登录用于是否点赞过
        Long loginUserId = ReqInfoContext.getReqInfo().getUserId();
        if (loginUserId != null) {
            // 判断当前用户是否点过赞
            UserFootDO foot = userFootService.queryUserFoot(comment.getCommentId(), DocumentTypeEnum.COMMENT.getCode(), loginUserId);
            comment.setPraised(foot != null && Objects.equals(foot.getPraiseStat(), PraiseStatEnum.PRAISE.getCode()));
        } else {
            comment.setPraised(false);
        }
    }

    /**
     * 查询回帖最多的评论
     *
     * @param articleId
     * @return
     */
    @Override
    public TopCommentDTO queryHotComment(Long articleId) {
        CommentDO comment = commentDao.getHotComment(articleId);
        if (comment == null) {
            return null;
        }

        TopCommentDTO result = CommentConverter.toTopDto(comment);
        // 查询子评论
        List<CommentDO> subComments = commentDao.listSubCommentIdMappers(articleId, Collections.singletonList(comment.getId()));
        List<SubCommentDTO> subs = subComments.stream().map(CommentConverter::toSubDto).collect(Collectors.toList());
        result.setChildComments(subs);

        // 填充评论信息
        fillTopCommentInfo(result);
        return result;
    }

    @Override
    public int queryCommentCount(Long articleId) {
        return commentDao.commentCount(articleId);
    }
}