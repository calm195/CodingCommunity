package cor.chrissy.community.web.front.comment;

import cor.chrissy.community.common.req.PageParam;
import cor.chrissy.community.common.req.comment.CommentSaveReq;
import cor.chrissy.community.service.comment.dto.CommentTreeDTO;
import cor.chrissy.community.service.comment.service.CommentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.util.Map;

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
    public String list(Long articleId, Long pageNum, Long pageSize, Model model) throws Exception {
        if (articleId == null) {
            throw new Exception("入参错误");
        }
        pageNum = (pageNum == null) ? 1L : pageNum;
        pageSize = (pageSize == null) ? 10L : pageSize;
        Map<Long, CommentTreeDTO> commentTreeDTOMap = commentService.getCommentList(articleId, PageParam.newPageInstance(pageNum, pageSize));
        model.addAttribute("comment", commentTreeDTOMap);
        return "biz/comment/list";
    }

    /**
     * 保存评论（TODO：异常需要捕获）
     *
     * @param commentSaveReq
     * @return
     * @throws Exception
     */
    @PostMapping(path = "save")
    public String save(CommentSaveReq commentSaveReq) throws Exception {
        commentService.saveComment(commentSaveReq);
        return "biz/comment/list";
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
