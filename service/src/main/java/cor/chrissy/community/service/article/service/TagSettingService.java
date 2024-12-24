package cor.chrissy.community.service.article.service;

import cor.chrissy.community.common.req.PageParam;
import cor.chrissy.community.common.req.article.TagReq;
import cor.chrissy.community.common.vo.PageVo;
import cor.chrissy.community.service.article.dto.TagDTO;

/**
 * @author wx128
 * @createAt 2024/12/18
 */
public interface TagSettingService {

    void saveTag(TagReq tagReq);

    void deleteTag(Integer tagId);

    void operateTag(Integer tagId, Integer pushStatus);

    /**
     * 获取tag列表
     *
     * @param pageParam
     * @return
     */
    PageVo<TagDTO> getTagList(PageParam pageParam);
}
