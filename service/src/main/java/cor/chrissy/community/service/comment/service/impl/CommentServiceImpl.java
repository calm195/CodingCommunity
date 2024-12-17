package cor.chrissy.community.service.comment.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import cor.chrissy.community.common.entity.BaseUserInfoDTO;
import cor.chrissy.community.common.enums.YesOrNoEnum;
import cor.chrissy.community.common.req.PageParam;
import cor.chrissy.community.common.req.comment.CommentSaveReq;
import cor.chrissy.community.core.util.NumUtil;
import cor.chrissy.community.service.comment.converter.CommentConverter;
import cor.chrissy.community.service.comment.dto.BaseCommentDTO;
import cor.chrissy.community.service.comment.dto.SubCommentDTO;
import cor.chrissy.community.service.comment.dto.TopCommentDTO;
import cor.chrissy.community.service.comment.repository.entity.CommentDO;
import cor.chrissy.community.service.comment.repository.mapper.CommentMapper;
import cor.chrissy.community.service.comment.service.CommentService;
import cor.chrissy.community.service.user.service.UserFootService;
import cor.chrissy.community.service.user.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author wx128
 * @createAt 2024/12/9
 */
@Service
public class CommentServiceImpl implements CommentService {
    @Resource
    private CommentMapper commentMapper;

    @Resource
    private CommentConverter commentConverter;

    @Resource
    private UserService userService;

    @Resource
    private UserFootService userFootService;

    @Override
    public List<TopCommentDTO> getArticleComments(Long articleId, PageParam pageParam) {
        List<CommentDO> commentDOS = queryTopCommentList(articleId, pageParam);
        if (CollectionUtils.isEmpty(commentDOS)) {
            return Collections.emptyList();
        }

        Map<Long, TopCommentDTO> commentFirstLevelMap = commentDOS.stream().collect(Collectors.toMap(CommentDO::getId, commentConverter::toTopCommentDTO));

        List<CommentDO> subCommentDOS = this.querySubCommentIdMappers(articleId, commentFirstLevelMap.keySet());
        Map<Long, SubCommentDTO> subCommentDTOMap = subCommentDOS.stream().collect(Collectors.toMap(CommentDO::getId, commentConverter::toSubCommentDTO));;

        subCommentDOS.forEach( comment -> {
            TopCommentDTO topCommentDTO = commentFirstLevelMap.get(comment.getTopCommentId());
            if (topCommentDTO != null) {
                SubCommentDTO subCommentDTO = subCommentDTOMap.get(comment.getId());
                topCommentDTO.getChildComments().add(subCommentDTO);
                if (Objects.equals(comment.getTopCommentId(), comment.getParentCommentId())) {
                    return;
                }

                SubCommentDTO parent = subCommentDTOMap.get(comment.getParentCommentId());
                if (parent != null) {
                    subCommentDTO.setParentContent(parent.getCommentContent());
                } else {
                    subCommentDTO.setParentContent("deleted.");
                }
            }
        });

        List<TopCommentDTO> result = new ArrayList<>();
        commentDOS.forEach(comment -> {
            TopCommentDTO dto = commentFirstLevelMap.get(comment.getId());
            fillCommentInfo(dto);
            dto.getChildComments().forEach(this::fillCommentInfo);
            Collections.sort(dto.getChildComments());
            result.add(dto);
        });

        Collections.sort(result);
        return result;
    }

    private void fillCommentInfo(BaseCommentDTO comment){
        BaseUserInfoDTO userInfoDTO = userService.getUserInfoByUserId(comment.getUserId());
        if (userInfoDTO == null) {
            comment.setUserName("deleted.");
            comment.setUserPhoto("");
            if (comment instanceof TopCommentDTO){
                ((TopCommentDTO) comment).setCommentCount(0);
            }
        } else {
            comment.setUserName(userInfoDTO.getUserName());
            comment.setUserPhoto(userInfoDTO.getPhoto());
            if (comment instanceof TopCommentDTO) {
                ((TopCommentDTO) comment).setCommentCount(((TopCommentDTO) comment).getChildComments().size());
            }
        }

        Long praiseCount = userFootService.queryCommentPraiseCount(comment.getCommentId());
        comment.setPraiseCount(praiseCount.intValue());
    }



    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long saveComment(CommentSaveReq commentSaveReq) {
        CommentDO commentDO = new CommentDO();
        if (NumUtil.nullOrZero(commentSaveReq.getCommentId())) {
            commentDO = addComment(commentSaveReq);
        } else {
            commentDO = updateComment(commentSaveReq);
        }
        return commentDO.getId();
    }

    private CommentDO addComment(CommentSaveReq commentSaveReq) {
        Long parentCommentAuthorId = getParentCommentUser(commentSaveReq.getParentCommentId());
        CommentDO commentDO = commentConverter.toDo(commentSaveReq);
        commentDO.setCreateTime(new Date());
        commentDO.setUpdateTime(new Date());
        commentMapper.insert(commentDO);

        userFootService.saveCommentFoot(commentDO, commentSaveReq.getUserId(), parentCommentAuthorId);
        return commentDO;
    }

    private CommentDO updateComment(CommentSaveReq commentSaveReq) {
        CommentDO commentDO = commentMapper.selectById(commentSaveReq.getCommentId());
        if (commentDO == null) {
            throw new RuntimeException("the comment id " + commentSaveReq.getCommentId() + " does not exist");
        }

        commentDO.setContent(commentSaveReq.getCommentContent());
        commentMapper.updateById(commentDO);
        return commentDO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteComment(Long commentId) throws Exception {

        CommentDO commentDO = commentMapper.selectById(commentId);
        if (commentDO == null) {
            throw new Exception("未查询到该评论");
        }

        commentDO.setDeleted(YesOrNoEnum.YES.getCode());
        commentMapper.updateById(commentDO);
        userFootService.deleteCommentFoot(commentDO, commentDO.getArticleId(), getParentCommentUser(commentDO.getParentCommentId()));
    }

    private Long getParentCommentUser(Long parentCommentId) {
        Long parentCommentAuthorId = null;
        if (NumUtil.upZero(parentCommentId)) {
            CommentDO parentComment = commentMapper.selectById(parentCommentId);
            if (parentComment == null) {
                throw new IllegalStateException("parent comment id " + parentCommentId + " does not exist");
            }
            parentCommentAuthorId = parentComment.getUserId();
        }
        return parentCommentAuthorId;
    }

    /**
     * 获取评论列表
     * @param articleId
     * @param pageParam
     * @return
     */
    private List<CommentDO> queryTopCommentList(Long articleId, PageParam pageParam) {
        QueryWrapper<CommentDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(CommentDO::getTopCommentId, 0)
                .eq(CommentDO::getArticleId, articleId)
                .eq(CommentDO::getDeleted, YesOrNoEnum.NO.getCode())
                .last(PageParam.getLimitSql(pageParam))
                .orderByDesc(CommentDO::getId);
        return commentMapper.selectList(queryWrapper);
    }

    private List<CommentDO> querySubCommentIdMappers(Long articleId, Collection<Long> topCommentIds) {
        QueryWrapper<CommentDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .in(CommentDO::getTopCommentId, topCommentIds)
                .eq(CommentDO::getArticleId, articleId)
                .eq(CommentDO::getDeleted, YesOrNoEnum.NO.getCode());
        return commentMapper.selectList(queryWrapper);
    }
}

