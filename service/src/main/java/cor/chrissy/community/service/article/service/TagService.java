package cor.chrissy.community.service.article.service;

import cor.chrissy.community.common.req.PageParam;
import cor.chrissy.community.common.vo.PageVo;
import cor.chrissy.community.service.article.dto.TagDTO;

/**
 * @author wx128
 * @createAt 2024/12/18
 */
public interface TagService {

    PageVo<TagDTO> queryTags(String key, PageParam param);

    Long queryTagId(String tagName);
}
