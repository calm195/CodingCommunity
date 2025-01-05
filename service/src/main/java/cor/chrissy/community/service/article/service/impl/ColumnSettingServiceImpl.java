package cor.chrissy.community.service.article.service.impl;

import cor.chrissy.community.common.entity.BaseUserInfoDTO;
import cor.chrissy.community.common.enums.StatusEnum;
import cor.chrissy.community.common.req.PageParam;
import cor.chrissy.community.common.req.article.ColumnArticleReq;
import cor.chrissy.community.common.req.article.ColumnReq;
import cor.chrissy.community.common.vo.PageVo;
import cor.chrissy.community.core.util.ExceptionUtil;
import cor.chrissy.community.core.util.NumUtil;
import cor.chrissy.community.service.article.conveter.ColumnConverter;
import cor.chrissy.community.service.article.dto.ColumnArticleDTO;
import cor.chrissy.community.service.article.dto.ColumnDTO;
import cor.chrissy.community.service.article.repository.dao.ArticleDao;
import cor.chrissy.community.service.article.repository.dao.ColumnDao;
import cor.chrissy.community.service.article.repository.entity.ArticleDO;
import cor.chrissy.community.service.article.repository.entity.ColumnArticleDO;
import cor.chrissy.community.service.article.repository.entity.ColumnInfoDO;
import cor.chrissy.community.service.article.repository.mapper.ColumnArticleMapper;
import cor.chrissy.community.service.article.service.ColumnService;
import cor.chrissy.community.service.article.service.ColumnSettingService;
import cor.chrissy.community.service.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wx128
 * @createAt 2024/12/18
 */
@Service
public class ColumnSettingServiceImpl implements ColumnSettingService {

    @Autowired
    private ColumnService columnService;

    @Autowired
    private ColumnDao columnDao;

    @Autowired
    private UserService userService;

    @Autowired
    private ColumnArticleMapper columnArticleMapper;
    @Autowired
    private ArticleDao articleDao;

    @Override
    public void saveColumn(ColumnReq req) {
        ColumnInfoDO columnInfoDO = ColumnConverter.toDo(req);
        if (NumUtil.nullOrZero(req.getColumnId())) {
            columnDao.save(columnInfoDO);
        } else {
            columnInfoDO.setId(req.getColumnId());
            columnDao.updateById(columnInfoDO);
        }
    }

    @Override
    public void saveColumnArticle(ColumnArticleReq req) {
        ColumnArticleDO columnArticleDO = ColumnConverter.toDo(req);
        if (NumUtil.nullOrZero(req.getId())) {
            columnArticleMapper.insert(columnArticleDO);
        } else {
            columnArticleDO.setId(req.getId());
            columnArticleMapper.updateById(columnArticleDO);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void sortColumnArticle(List<ColumnArticleReq> columnArticleReqs) {
        columnArticleReqs.forEach(columnArticleReq -> {
            ColumnArticleDO columnArticleDO = columnArticleMapper.selectById(columnArticleReq.getId());
            columnArticleDO.setSection(columnArticleReq.getSort());
            columnArticleMapper.updateById(columnArticleDO);
        });
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteColumn(Integer columnId) {
        columnDao.deleteColumn(columnId);
    }

    @Override
    public void deleteColumnArticle(Integer id) {
        ColumnArticleDO columnArticleDO = columnArticleMapper.selectById(id);
        if (columnArticleDO != null) {
            columnArticleMapper.deleteById(id);
        }
    }

    @Override
    public PageVo<ColumnDTO> listColumn(PageParam pageParam) {
        List<ColumnInfoDO> columnList = columnDao.listColumns(pageParam);
        List<ColumnDTO> columnDTOS = ColumnConverter.toDtos(columnList);
        columnDTOS.forEach(columnDTO -> {
            BaseUserInfoDTO user = userService.queryBasicUserInfo(columnDTO.getAuthor());
            columnDTO.setAuthorName(user.getUserName());
            columnDTO.setAuthorAvatar(user.getPhoto());
            columnDTO.setAuthorProfile(user.getProfile());
        });
        Integer totalCount = columnDao.countColumns();
        return PageVo.build(columnDTOS, pageParam.getPageSize(), pageParam.getPageNum(), totalCount);
    }

    @Override
    public PageVo<ColumnArticleDTO> queryColumnArticles(long columnId, PageParam pageParam) throws Exception {
        List<ColumnArticleDTO> simpleArticleDTOS = new ArrayList<>();
        List<ColumnArticleDO> columnArticleDOS = columnDao.listColumnArticlesDetail(columnId, pageParam);
        for (ColumnArticleDO columnArticleDO : columnArticleDOS) {
            ArticleDO articleDO = articleDao.getById(columnArticleDO.getArticleId());
            if (articleDO == null) {
                throw ExceptionUtil.of(StatusEnum.ARTICLE_NOT_EXISTS, columnArticleDO.getArticleId());
            }
            ColumnInfoDO columnInfoDO = columnDao.getById(columnArticleDO.getColumnId());
            if (columnInfoDO == null) {
                throw ExceptionUtil.of(StatusEnum.COLUMN_NOT_EXISTS, columnArticleDO.getColumnId());
            }

            ColumnArticleDTO columnArticleDTO = new ColumnArticleDTO();
            columnArticleDTO.setId(columnArticleDO.getId());
            columnArticleDTO.setArticleId(articleDO.getId());
            columnArticleDTO.setTitle(articleDO.getTitle());
            columnArticleDTO.setSort(columnArticleDO.getSection());
            columnArticleDTO.setColumnId(columnArticleDO.getColumnId());
            columnArticleDTO.setColumn(columnInfoDO.getColumnName());
            simpleArticleDTOS.add(columnArticleDTO);
        }

        int totalCount = columnDao.countColumnArticles(columnId);
        return PageVo.build(simpleArticleDTOS, pageParam.getPageSize(), pageParam.getPageNum(), totalCount);
    }
}

