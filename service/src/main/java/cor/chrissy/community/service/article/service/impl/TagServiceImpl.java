package cor.chrissy.community.service.article.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import cor.chrissy.community.core.enums.PushStatusEnum;
import cor.chrissy.community.core.enums.YesOrNoEnum;
import cor.chrissy.community.core.req.PageParam;
import cor.chrissy.community.service.article.repository.entity.TagDO;
import cor.chrissy.community.service.article.repository.mapper.TagMapper;
import cor.chrissy.community.service.article.service.TagService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author wx128
 * @date 2024/12/9
 */
@Service
public class TagServiceImpl implements TagService {
    @Resource
    private TagMapper tagMapper;

    @Override
    public Long addTag(TagDO tagDTO) {
        tagMapper.insert(tagDTO);
        return tagDTO.getId();
    }

    @Override
    public void updateTag(Long tagId, String tagName) {
        TagDO tagDTO = tagMapper.selectById(tagId);
        if (tagDTO != null) {
            tagDTO.setTagName(tagName);
            tagDTO.setStatus(YesOrNoEnum.NO.getCode());
            tagMapper.updateById(tagDTO);
        }
    }

    @Override
    public void deleteTag(Long tagId) {
        TagDO tagDTO = tagMapper.selectById(tagId);
        if (tagDTO != null) {
            tagDTO.setDeleted(YesOrNoEnum.YES.getCode());
            tagMapper.updateById(tagDTO);
        }
    }

    @Override
    public void operateTag(Long tagId, PushStatusEnum pushStatusEnum) {
        TagDO tagDTO = tagMapper.selectById(tagId);
        if (tagDTO != null) {
            tagDTO.setStatus(pushStatusEnum.getCode());
            tagMapper.updateById(tagDTO);
        }
    }

    @Override
    public IPage<TagDO> getTagByPage(PageParam pageParam) {
        LambdaQueryWrapper<TagDO> query = Wrappers.lambdaQuery();
        query.eq(TagDO::getDeleted, YesOrNoEnum.NO.getCode())
                .eq(TagDO::getStatus, PushStatusEnum.ONLINE.getCode());
        Page<TagDO> page = new Page<>(pageParam.getPageNum(), pageParam.getPageSize());
        return tagMapper.selectPage(page, query);
    }

    @Override
    public List<TagDO> getTagListByCategoryId(Long categoryId) {
        LambdaQueryWrapper<TagDO> query = Wrappers.lambdaQuery();
        query.eq(TagDO::getDeleted, YesOrNoEnum.NO.getCode())
                .eq(TagDO::getCategoryId, categoryId);
        return tagMapper.selectList(query);
    }
}
