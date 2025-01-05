package cor.chrissy.community.service.article.service.impl;

import cor.chrissy.community.common.req.PageParam;
import cor.chrissy.community.common.vo.PageVo;
import cor.chrissy.community.service.article.dto.TagDTO;
import cor.chrissy.community.service.article.repository.dao.TagDao;
import cor.chrissy.community.service.article.service.TagService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author wx128
 * @createAt 2024/12/18
 */
@Service
public class TagServiceImpl implements TagService {
    private final TagDao tagDao;

    public TagServiceImpl(TagDao tagDao) {
        this.tagDao = tagDao;
    }

    @Override
    public PageVo<TagDTO> queryTags(String key, PageParam param) {
        List<TagDTO> tagDTOS = tagDao.listOnlineTag(key, param);
        int total = tagDao.countOnlineTag(key);
        return PageVo.build(tagDTOS, param.getPageSize(), param.getPageNum(), total);
    }

    @Override
    public Long queryTagId(String tagName) {
        return tagDao.selectTagIdByTag(tagName);
    }
}
