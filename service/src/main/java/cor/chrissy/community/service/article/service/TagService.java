package cor.chrissy.community.service.article.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import cor.chrissy.community.core.enums.PushStatusEnum;
import cor.chrissy.community.core.req.PageParam;
import cor.chrissy.community.service.article.repository.entity.TagDO;

import java.util.List;

/**
 * @author wx128
 * @date 2024/12/9
 */
public interface TagService {
    /**
     * 添加标签
     *
     * @param tagDO
     * @return
     */
    Long addTag(TagDO tagDO);

    /**
     * 更新标签
     *
     * @param tagId
     * @param tagName
     */
    void updateTag(Long tagId, String tagName);

    /**
     * 删除标签
     *
     * @param tagId
     */
    void deleteTag(Long tagId);

    /**
     * 上线/下线标签
     *
     * @param tagId
     */
    void operateTag(Long tagId, PushStatusEnum pushStatusEnum);

    /**
     * 标签分页查询
     *
     * @return
     */
    IPage<TagDO> getTagByPage(PageParam pageParam);

    /**
     * 根据类目ID查询标签列表
     *
     * @param categoryId
     * @return
     */
    List<TagDO> getTagListByCategoryId(Long categoryId);
}
