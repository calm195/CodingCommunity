package cor.chrissy.community.service.article.service.impl;

import cor.chrissy.community.common.req.PageParam;
import cor.chrissy.community.common.req.article.TagReq;
import cor.chrissy.community.common.vo.PageVo;
import cor.chrissy.community.core.util.NumUtil;
import cor.chrissy.community.service.article.conveter.ArticleConverter;
import cor.chrissy.community.service.article.dto.TagDTO;
import cor.chrissy.community.service.article.repository.dao.CategoryDao;
import cor.chrissy.community.service.article.repository.dao.TagDao;
import cor.chrissy.community.service.article.repository.entity.CategoryDO;
import cor.chrissy.community.service.article.repository.entity.TagDO;
import cor.chrissy.community.service.article.service.TagSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author wx128
 * @createAt 2024/12/18
 */
@Service
public class TagSettingServiceImpl implements TagSettingService {

    @Autowired
    private TagDao tagDao;

    @Autowired
    private CategoryDao categoryDao;

    @Override
    public void saveTag(TagReq tagReq) {
        TagDO tagDO = ArticleConverter.toDO(tagReq);
        if (NumUtil.nullOrZero(tagReq.getTagId())) {
            tagDao.save(tagDO);
        } else {
            tagDO.setId(tagReq.getTagId());
            tagDao.updateById(tagDO);
        }
    }

    @Override
    public void deleteTag(Integer tagId) {
        TagDO tagDO = tagDao.getById(tagId);
        if (tagDO != null){
            tagDao.removeById(tagId);
        }
    }

    @Override
    public void operateTag(Integer tagId, Integer pushStatus) {
        TagDO tagDO = tagDao.getById(tagId);
        if (tagDO != null){
            tagDO.setStatus(pushStatus);
            tagDao.updateById(tagDO);
        }
    }

    @Override
    public PageVo<TagDTO> getTagList(PageParam pageParam) {
        List<TagDTO> tagDTOS = tagDao.listTag(pageParam);
        for (TagDTO tagDTO : tagDTOS) {
            CategoryDO categoryDO = categoryDao.getById(tagDTO.getCategoryId());
            tagDTO.setCategoryName(categoryDO.getCategoryName());
        }
        Integer totalCount = tagDao.countTag();
        return PageVo.build(tagDTOS, pageParam.getPageSize(), pageParam.getPageNum(), totalCount);
    }
}
