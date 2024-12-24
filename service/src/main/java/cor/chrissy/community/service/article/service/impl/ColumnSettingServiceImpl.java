package cor.chrissy.community.service.article.service.impl;

import cor.chrissy.community.common.entity.BaseUserInfoDTO;
import cor.chrissy.community.common.req.PageParam;
import cor.chrissy.community.common.req.article.ColumnArticleReq;
import cor.chrissy.community.common.req.article.ColumnReq;
import cor.chrissy.community.common.vo.PageVo;
import cor.chrissy.community.core.util.NumUtil;
import cor.chrissy.community.service.article.conveter.ColumnConverter;
import cor.chrissy.community.service.article.dto.ArticleDTO;
import cor.chrissy.community.service.article.dto.ColumnDTO;
import cor.chrissy.community.service.article.repository.dao.ColumnDao;
import cor.chrissy.community.service.article.repository.entity.ColumnArticleDO;
import cor.chrissy.community.service.article.repository.entity.ColumnInfoDO;
import cor.chrissy.community.service.article.repository.mapper.ColumnArticleMapper;
import cor.chrissy.community.service.article.service.ColumnService;
import cor.chrissy.community.service.article.service.ColumnSettingService;
import cor.chrissy.community.service.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
            columnArticleDO.setSection(columnArticleReq.getSection());
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
    public List<ArticleDTO> queryColumnArticles(long columnId) {
        return columnService.queryColumnArticlesDetail(columnId);
    }
}

