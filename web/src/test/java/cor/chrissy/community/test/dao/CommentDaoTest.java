package cor.chrissy.community.test.dao;

import cor.chrissy.community.service.comment.dto.CommentTreeDTO;
import cor.chrissy.community.service.comment.service.impl.CommentServiceImpl;
import cor.chrissy.community.common.req.comment.CommentReq;
import cor.chrissy.community.common.req.PageSearchReq;
import cor.chrissy.community.test.BasicTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * @author wx128
 * @createAt 2024/12/10
 */
@Slf4j
public class CommentDaoTest extends BasicTest {

    @Autowired
    private CommentServiceImpl commentService;

    @Test
    public void testSaveComment() throws Exception {
        CommentReq commentReq = new CommentReq();
        commentReq.setArticleId(12L);
        commentReq.setCommentContent("comment test1");
        commentReq.setParentCommentId(0L);
        commentReq.setUserId(123L);
        commentService.saveComment(commentReq);
    }

    @Test
    public void testGetCommentList() {
        PageSearchReq pageSearchReq = new PageSearchReq();
        pageSearchReq.setPageNum(1L);
        pageSearchReq.setPageSize(2L);
        Map<Long, CommentTreeDTO> commentTreeDTOList = commentService.getCommentList(12L, pageSearchReq);
        log.info("commentTreeDTOList: {}", commentTreeDTOList);
    }
}

