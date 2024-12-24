package cor.chrissy.community.service.article.service;

import cor.chrissy.community.service.article.dto.TagDTO;

import java.util.List;

/**
 * @author wx128
 * @createAt 2024/12/18
 */
public interface TagService {
    /**
     * 根据类目ID查询标签列表
     *
     * @param categoryId
     * @return
     */
    List<TagDTO> queryTagsByCategoryId(Long categoryId);

    Long queryTagId(String tagName);
}
