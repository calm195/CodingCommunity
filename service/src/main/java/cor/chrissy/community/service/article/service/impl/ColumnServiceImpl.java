package cor.chrissy.community.service.article.service.impl;

import cor.chrissy.community.common.entity.BaseDO;
import cor.chrissy.community.common.entity.BaseUserInfoDTO;
import cor.chrissy.community.common.enums.StatusEnum;
import cor.chrissy.community.common.req.PageParam;
import cor.chrissy.community.common.vo.PageListVo;
import cor.chrissy.community.core.util.ExceptionUtil;
import cor.chrissy.community.service.article.conveter.ColumnConverter;
import cor.chrissy.community.service.article.dto.ColumnDTO;
import cor.chrissy.community.service.article.dto.ColumnFootCountDTO;
import cor.chrissy.community.service.article.dto.SimpleArticleDTO;
import cor.chrissy.community.service.article.repository.dao.ArticleDao;
import cor.chrissy.community.service.article.repository.dao.ColumnDao;
import cor.chrissy.community.service.article.repository.entity.ArticleDO;
import cor.chrissy.community.service.article.repository.entity.ColumnInfoDO;
import cor.chrissy.community.service.article.service.ColumnService;
import cor.chrissy.community.service.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author wx128
 * @createAt 2024/12/18
 */
@Service
public class ColumnServiceImpl implements ColumnService {

    @Autowired
    private ColumnDao columnDao;

    @Autowired
    private ArticleDao articleDao;

    @Autowired
    private UserService userService;

    /**
     * 专栏列表
     *
     * @return
     */
    @Override
    public PageListVo<ColumnDTO> listColumn(PageParam pageParam) {
        List<ColumnInfoDO> columnList = columnDao.listOnlineColumns(pageParam);
        List<ColumnDTO> result = new ArrayList<>();
        columnList.forEach(info -> {
            ColumnDTO dto = ColumnConverter.toDto(info);
            fillColumnInfo(dto);
            result.add(dto);
        });
        return PageListVo.newVo(result, pageParam.getPageSize());
    }

    private void fillColumnInfo(ColumnDTO dto) {
        // 补齐专栏对应的用户信息
        BaseUserInfoDTO user = userService.queryBasicUserInfo(dto.getAuthor());
        dto.setAuthorName(user.getUserName());
        dto.setAuthorAvatar(user.getPhoto());
        dto.setAuthorProfile(user.getProfile());

        // 统计计数，这里先只返回文章数
        ColumnFootCountDTO countDTO = new ColumnFootCountDTO();
        countDTO.setArticleCount(columnDao.countColumnArticles(dto.getColumnId()));
        dto.setCount(countDTO);
    }

    @Override
    public ColumnDTO queryColumnInfo(Long columnId) {
        ColumnInfoDO column = columnDao.getById(columnId);
        ColumnDTO dto = ColumnConverter.toDto(column);
        fillColumnInfo(dto);
        return dto;
    }

    @Override
    public Long queryColumnArticle(long columnId, Integer section) {
        Long articleId = columnDao.getColumnArticleId(columnId, section);
        if (articleId == null) {
            throw ExceptionUtil.of(StatusEnum.RECORDS_NOT_EXISTS, "专栏还没有更新到这一章哦");
        }
        return articleId;
    }

    /**
     * 查询专栏的文章详情
     *
     * @param columnId
     * @return
     */
    @Override
    public List<SimpleArticleDTO> queryColumnArticles(long columnId) {
        List<Long> articleIds = columnDao.listColumnArticles(columnId);
        List<ArticleDO> articles = articleDao.listByIds(articleIds);
        Map<Long, SimpleArticleDTO> articleMap = articles.stream().collect(Collectors.toMap(BaseDO::getId, s -> {
            SimpleArticleDTO simple = new SimpleArticleDTO();
            simple.setId(s.getId());
            simple.setTitle(s.getShortTitle());
            simple.setCreateTime(new Timestamp(s.getCreateTime().getTime()));
            return simple;
        }));
        List<SimpleArticleDTO> articleList = new ArrayList<>();
        articleIds.forEach(id -> Optional.ofNullable(articleMap.get(id)).ifPresent(articleList::add));
        return articleList;
    }

}

