package cor.chrissy.community.service.notify.service.impl;

import cor.chrissy.community.common.enums.NotifyStatEnum;
import cor.chrissy.community.common.notify.NotifyMsgEvent;
import cor.chrissy.community.service.article.repository.entity.ArticleDO;
import cor.chrissy.community.service.article.service.ArticleReadService;
import cor.chrissy.community.service.comment.repository.entity.CommentDO;
import cor.chrissy.community.service.comment.service.CommentReadService;
import cor.chrissy.community.service.notify.repository.dao.NotifyMsgDao;
import cor.chrissy.community.service.notify.repository.entity.NotifyMsgDO;
import cor.chrissy.community.service.user.repository.entity.UserFootDO;
import cor.chrissy.community.service.user.repository.entity.UserRelationDO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * @author wx128
 * @createAt 2024/12/18
 */
@Slf4j
@Async
@Service
public class NotifyMsgListener<T> implements ApplicationListener<NotifyMsgEvent<T>> {
    private final ArticleReadService articleReadService;

    private final CommentReadService commentReadService;

    private final NotifyMsgDao notifyMsgDao;

    public NotifyMsgListener(ArticleReadService articleReadService,
                             CommentReadService commentReadService,
                             NotifyMsgDao notifyMsgDao) {
        this.articleReadService = articleReadService;
        this.commentReadService = commentReadService;
        this.notifyMsgDao = notifyMsgDao;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onApplicationEvent(NotifyMsgEvent<T> msgEvent) {
        switch (msgEvent.getNotifyType()) {
            case COMMENT:
                saveCommentNotify((NotifyMsgEvent<CommentDO>) msgEvent);
                break;
            case REPLY:
                saveReplyNotify((NotifyMsgEvent<CommentDO>) msgEvent);
                break;
            case PRAISE:
            case COLLECT:
                saveArticleNotify((NotifyMsgEvent<UserFootDO>) msgEvent);
                break;
            case FOLLOW:
                saveFollowNotify((NotifyMsgEvent<UserRelationDO>) msgEvent);
                break;
            case CANCEL_PRAISE:
            case CANCEL_COLLECT:
            case CANCEL_FOLLOW:
                // todo 取消操作，若之前的消息是未读状态，则移除对应的记录
                log.info("取消操作: {}", msgEvent);
            default:
                // todo 系统消息
        }
    }

    /**
     * 评论 + 回复
     *
     * @param event
     */
    private void saveCommentNotify(NotifyMsgEvent<CommentDO> event) {
        NotifyMsgDO msg = new NotifyMsgDO();
        CommentDO comment = event.getContent();
        ArticleDO article = articleReadService.queryBasicArticle(comment.getArticleId());
        msg.setNotifyUserId(article.getAuthorId())
                .setOperateUserId(comment.getUserId())
                .setRelatedId(article.getId())
                .setType(event.getNotifyType().getType())
                .setState(NotifyStatEnum.UNREAD.getStat())
                .setMsg(comment.getContent());
        // 对于评论而言，支持多次评论；因此若之前有也不删除
        notifyMsgDao.save(msg);
    }

    /**
     * 评论回复消息
     *
     * @param event
     */
    private void saveReplyNotify(NotifyMsgEvent<CommentDO> event) {
        NotifyMsgDO msg = new NotifyMsgDO();
        CommentDO comment = event.getContent();
        CommentDO parent = commentReadService.queryComment(comment.getParentCommentId());
        msg.setNotifyUserId(parent.getUserId())
                .setOperateUserId(comment.getUserId())
                .setRelatedId(comment.getArticleId())
                .setType(event.getNotifyType().getType())
                .setState(NotifyStatEnum.UNREAD.getStat())
                .setMsg(comment.getContent());
        // 回复同样支持多次回复，不做幂等校验
        notifyMsgDao.save(msg);
    }

    /**
     * 点赞 + 收藏
     *
     * @param event
     */
    private void saveArticleNotify(NotifyMsgEvent<UserFootDO> event) {
        UserFootDO foot = event.getContent();
        NotifyMsgDO msg = new NotifyMsgDO().setRelatedId(foot.getDocumentId())
                .setNotifyUserId(foot.getDocumentAuthorId())
                .setOperateUserId(foot.getUserId())
                .setType(event.getNotifyType().getType())
                .setState(NotifyStatEnum.UNREAD.getStat())
                .setMsg("");
        NotifyMsgDO record = notifyMsgDao.getByUserIdRelatedIdAndType(msg);
        if (record == null) {
            // 若之前已经有对应的通知，则不重复记录；因为一个用户对一篇文章，可以重复的点赞、取消点赞，但是最终我们只通知一次
            notifyMsgDao.save(msg);
        }
    }

    /**
     * 关注
     *
     * @param event
     */
    private void saveFollowNotify(NotifyMsgEvent<UserRelationDO> event) {
        UserRelationDO relation = event.getContent();
        NotifyMsgDO msg = new NotifyMsgDO().setRelatedId(0L)
                .setNotifyUserId(relation.getUserId())
                .setOperateUserId(relation.getFollowUserId())
                .setType(event.getNotifyType().getType())
                .setState(NotifyStatEnum.UNREAD.getStat())
                .setMsg("");
        NotifyMsgDO record = notifyMsgDao.getByUserIdRelatedIdAndType(msg);
        if (record == null) {
            // 若之前已经有对应的通知，则不重复记录；因为用户的关注是一对一的，可以重复的关注、取消，但是最终我们只通知一次
            notifyMsgDao.save(msg);
        }
    }
}

