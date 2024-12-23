package cor.chrissy.community.service.article.conveter;

import cor.chrissy.community.common.enums.ArticleTypeEnum;
import cor.chrissy.community.common.enums.FlagBitEnum;
import cor.chrissy.community.common.enums.SourceTypeEnum;
import cor.chrissy.community.common.enums.YesOrNoEnum;
import cor.chrissy.community.common.req.article.ArticlePostReq;
import cor.chrissy.community.common.req.article.CategoryReq;
import cor.chrissy.community.common.req.article.TagReq;
import cor.chrissy.community.service.article.dto.ArticleDTO;
import cor.chrissy.community.service.article.dto.CategoryDTO;
import cor.chrissy.community.service.article.dto.TagDTO;
import cor.chrissy.community.service.article.repository.entity.ArticleDO;
import cor.chrissy.community.service.article.repository.entity.CategoryDO;
import cor.chrissy.community.service.article.repository.entity.TagDO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 文章信息转化工具
 *
 * @author wx128
 * @createAt 2024/12/11
 */
@Service
public class ArticleConverter {

    public static ArticleDO toArticleDo(ArticlePostReq req, Long author) {
        ArticleDO article = new ArticleDO();
        // 设置作者ID
        article.setAuthorId(author);
        article.setId(req.getArticleId());
        article.setTitle(req.getTitle());
        article.setShortTitle(req.getSubTitle());
        article.setArticleType(ArticleTypeEnum.valueOf(req.getArticleType().toUpperCase()).getCode());
        article.setPicture(req.getCover() == null ? "" : req.getCover());
        article.setCategoryId(req.getCategoryId());
        article.setSource(req.getSource());
        article.setSourceUrl(req.getSourceUrl());
        article.setSummary(req.getSummary());
        article.setStatus(req.pushStatus().getCode());
        article.setDeleted(req.deleted() ? YesOrNoEnum.YES.getCode() : YesOrNoEnum.NO.getCode());
        return article;
    }

    public static ArticleDTO toDto(ArticleDO articleDO) {
        if (articleDO == null) {
            return null;
        }
        ArticleDTO articleDTO = new ArticleDTO();
        articleDTO.setAuthor(articleDO.getAuthorId());
        articleDTO.setArticleId(articleDO.getId());
        articleDTO.setArticleType(articleDO.getArticleType());
        articleDTO.setTitle(articleDO.getTitle());
        articleDTO.setShortTitle(articleDO.getShortTitle());
        articleDTO.setSummary(articleDO.getSummary());
        articleDTO.setCover(articleDO.getPicture());
        articleDTO.setSourceType(SourceTypeEnum.fromCode(articleDO.getSource()).getDesc());
        articleDTO.setSourceUrl(articleDO.getSourceUrl());
        articleDTO.setStatus(articleDO.getStatus());
        articleDTO.setFlagBit(articleDO.getFlagBit());
        articleDTO.setCreateTime(articleDO.getCreateTime().getTime());
        articleDTO.setLastUpdateTime(articleDO.getUpdateTime().getTime());
        articleDTO.setIsOfficial((articleDO.getFlagBit() & FlagBitEnum.OFFICIAL.getCode()) == 0 ? Boolean.FALSE : Boolean.TRUE);
        articleDTO.setIsTopping((articleDO.getFlagBit() & FlagBitEnum.TOPPING.getCode()) == 0 ? Boolean.FALSE : Boolean.TRUE);
        articleDTO.setIsCream((articleDO.getFlagBit() & FlagBitEnum.CREAM.getCode()) == 0 ? Boolean.FALSE : Boolean.TRUE);

        // 设置类目id
        articleDTO.setCategory(new CategoryDTO(articleDO.getCategoryId(), null));
        return articleDTO;
    }

    public static List<ArticleDTO> toArticleDtoList(List<ArticleDO> articleDOS) {
        return articleDOS.stream().map(ArticleConverter::toDto).collect(Collectors.toList());
    }

    /**
     * do转换
     *
     * @param tag
     * @return
     */
    public static TagDTO toDto(TagDO tag) {
        TagDTO dto = new TagDTO();
        dto.setTag(tag.getTagName());
        dto.setTagId(tag.getId());
        dto.setCategoryId(tag.getCategoryId());
        dto.setStatus(tag.getStatus());
        return dto;
    }

    public static List<TagDTO> toDtoList(List<TagDO> tags) {
        return tags.stream().map(ArticleConverter::toDto).collect(Collectors.toList());
    }


    public static CategoryDTO toDto(CategoryDO category) {
        CategoryDTO dto = new CategoryDTO();
        dto.setCategory(category.getCategoryName());
        dto.setCategoryId(category.getId());
        dto.setStatus(category.getStatus());
        dto.setSelected(false);
        return dto;
    }

    public static List<CategoryDTO> toCategoryDtoList(List<CategoryDO> categorys) {
        return categorys.stream().map(ArticleConverter::toDto).collect(Collectors.toList());
    }

    public static TagDO toDO(TagReq tagReq) {
        if (tagReq == null) {
            return null;
        }
        TagDO tagDO = new TagDO();
        tagDO.setTagName(tagReq.getTagName());
        tagDO.setTagType(tagReq.getTagType());
        tagDO.setCategoryId(tagReq.getCategoryId());
        return tagDO;
    }

    public static CategoryDO toDO(CategoryReq categoryReq) {
        if (categoryReq == null) {
            return null;
        }
        CategoryDO categoryDO = new CategoryDO();
        categoryDO.setCategoryName(categoryReq.getCategoryName());
        return categoryDO;
    }
}

