package cor.chrissy.community.web.front.comment.rest;

import cor.chrissy.community.common.context.ReqInfoContext;
import cor.chrissy.community.common.enums.DocumentTypeEnum;
import cor.chrissy.community.common.enums.NotifyTypeEnum;
import cor.chrissy.community.common.enums.OperateTypeEnum;
import cor.chrissy.community.common.enums.StatusEnum;
import cor.chrissy.community.common.notify.NotifyMsgEvent;
import cor.chrissy.community.common.req.PageParam;
import cor.chrissy.community.common.req.comment.CommentSaveReq;
import cor.chrissy.community.common.result.Result;
import cor.chrissy.community.core.permission.Permission;
import cor.chrissy.community.core.permission.UserRole;
import cor.chrissy.community.core.util.NumUtil;
import cor.chrissy.community.core.util.SpringUtil;
import cor.chrissy.community.service.comment.dto.TopCommentDTO;
import cor.chrissy.community.service.comment.repository.entity.CommentDO;
import cor.chrissy.community.service.comment.service.CommentReadService;
import cor.chrissy.community.service.comment.service.CommentWriteService;
import cor.chrissy.community.service.user.repository.entity.UserFootDO;
import cor.chrissy.community.service.user.service.UserFootService;
import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * @author wx128
 * @createAt 2024/12/19
 */
@RestController
@RequestMapping(path = "comment/api")
public class CommentRestController {

    @Autowired
    private CommentReadService commentReadService;

    @Autowired
    private CommentWriteService commentWriteService;

    @Autowired
    private UserFootService userFootService;

    /**
     * 评论列表页
     *
     * @param articleId
     * @return
     */
    @ResponseBody
    @RequestMapping(path = "list")
    public Result<List<TopCommentDTO>> list(Long articleId, Long pageNum, Long pageSize) {
        if (NumUtil.nullOrZero(articleId)) {
            return Result.fail(StatusEnum.ILLEGAL_ARGUMENTS_MIXED, "文章id为空");
        }
        pageNum = Optional.ofNullable(pageNum).orElse(PageParam.DEFAULT_PAGE_NUM);
        pageSize = Optional.ofNullable(pageSize).orElse(PageParam.DEFAULT_PAGE_SIZE);
        List<TopCommentDTO> result = commentReadService.getArticleComments(articleId, PageParam.newPageInstance(pageNum, pageSize));
        return Result.ok(result);
    }

    /**
     * 保存评论
     *
     * @param req
     * @return
     */
    @Permission(role = UserRole.LOGIN)
    @PostMapping(path = "post")
    @ResponseBody
    public Result<Boolean> save(@RequestBody CommentSaveReq req) {
        if (req.getArticleId() == null) {
            return Result.fail(StatusEnum.ILLEGAL_ARGUMENTS_MIXED, "文章id为空");
        }
        req.setUserId(ReqInfoContext.getReqInfo().getUserId());
        req.setCommentContent(StringEscapeUtils.escapeHtml3(req.getCommentContent()));
        Long commentId = commentWriteService.saveComment(req);
        return Result.ok(NumUtil.upZero(commentId));
    }

    /**
     * 删除评论
     *
     * @param commentId
     * @return
     */
    @Permission(role = UserRole.LOGIN)
    @RequestMapping(path = "delete")
    public Result<Boolean> delete(Long commentId) {
        commentWriteService.deleteComment(commentId, ReqInfoContext.getReqInfo().getUserId());
        return Result.ok(true);
    }

    /**
     * 收藏、点赞等相关操作
     *
     * @param commendId
     * @param type      取值来自于 OperateTypeEnum#code
     * @return
     */
    @Permission(role = UserRole.LOGIN)
    @GetMapping(path = "favor")
    public Result<Boolean> favor(@RequestParam(name = "commentId") Long commendId,
                                @RequestParam(name = "type") Integer type) {
        OperateTypeEnum operate = OperateTypeEnum.fromCode(type);
        if (operate == OperateTypeEnum.EMPTY) {
            return Result.fail(StatusEnum.ILLEGAL_ARGUMENTS_MIXED, type + "非法");
        }

        // 要求文章必须存在
        CommentDO comment = commentReadService.queryComment(commendId);
        if (comment == null) {
            return Result.fail(StatusEnum.ILLEGAL_ARGUMENTS_MIXED, "评论不存在!");
        }

        UserFootDO foot = userFootService.saveOrUpdateUserFoot(DocumentTypeEnum.COMMENT,
                commendId,
                comment.getUserId(),
                ReqInfoContext.getReqInfo().getUserId(),
                operate);
        // 点赞、收藏消息
        NotifyTypeEnum notifyType = OperateTypeEnum.getNotifyType(operate);
        Optional.ofNullable(notifyType).ifPresent(notify -> SpringUtil.publishEvent(new NotifyMsgEvent<>(this, notify, foot)));
        return Result.ok(true);
    }

}

