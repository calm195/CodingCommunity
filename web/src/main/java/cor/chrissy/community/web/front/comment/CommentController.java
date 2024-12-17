package cor.chrissy.community.web.front.comment;

import cor.chrissy.community.common.context.ReqInfoContext;
import cor.chrissy.community.common.enums.StatusEnum;
import cor.chrissy.community.common.req.PageParam;
import cor.chrissy.community.common.req.comment.CommentSaveReq;
import cor.chrissy.community.common.result.Result;
import cor.chrissy.community.core.permission.Permission;
import cor.chrissy.community.core.permission.UserRole;
import cor.chrissy.community.core.util.NumUtil;
import cor.chrissy.community.service.comment.dto.TopCommentDTO;
import cor.chrissy.community.service.comment.service.CommentService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author wx128
 * @createAt 2024/12/11
 */
@Controller
@RequestMapping(path = "comment")
@Slf4j
public class CommentController {
    @Resource
    private CommentService commentService;

    /**
     * 评论列表页（TODO：异常需要捕获）
     *
     * @param articleId
     * @return
     */
    @GetMapping(path = "list")
    public Result<List<TopCommentDTO>> list(Long articleId, Long pageNum, Long pageSize) throws Exception {
        if (NumUtil.nullOrZero(articleId)) {
            return Result.fail(StatusEnum.ILLEGAL_ARGUMENTS_MIXED, "文章id为空");
        }
        pageNum = (pageNum == null) ? 1L : pageNum;
        pageSize = (pageSize == null) ? 10L : pageSize;
        List<TopCommentDTO> result = commentService.getArticleComments(articleId, PageParam.newPageInstance(pageNum, pageSize));
        return Result.ok(result);
    }

    /**
     * 保存评论（TODO：异常需要捕获）
     *
     * @param commentSaveReq
     * @return
     * @throws Exception
     */
    @Permission(role = UserRole.LOGIN)
    @PostMapping(path = "save")
    @ResponseBody
    public Result<Boolean> save(@RequestBody CommentSaveReq commentSaveReq) throws Exception {
        if (commentSaveReq.getArticleId() == null) {
            return Result.fail(StatusEnum.ILLEGAL_ARGUMENTS_MIXED, "文章id为空");
        }
        commentSaveReq.setUserId(ReqInfoContext.getReqInfo().getUserId());
        commentSaveReq.setCommentContent(StringEscapeUtils.escapeHtml3(commentSaveReq.getCommentContent()));
        Long commentId = commentService.saveComment(commentSaveReq);
        return Result.ok(NumUtil.upZero(commentId));
    }

    /**
     * 删除评论（TODO：异常需要捕获）
     *
     * @param commentId
     * @return
     * @throws Exception
     */
    @PostMapping(path = "delete")
    public String delete(Long commentId) throws Exception {
        commentService.deleteComment(commentId);
        return "biz/comment/list";
    }
}
