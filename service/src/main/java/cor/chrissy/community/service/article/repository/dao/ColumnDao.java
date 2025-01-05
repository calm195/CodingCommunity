package cor.chrissy.community.service.article.repository.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cor.chrissy.community.common.enums.ColumnStatusEnum;
import cor.chrissy.community.common.req.PageParam;
import cor.chrissy.community.service.article.repository.entity.ColumnArticleDO;
import cor.chrissy.community.service.article.repository.entity.ColumnInfoDO;
import cor.chrissy.community.service.article.repository.mapper.ColumnArticleMapper;
import cor.chrissy.community.service.article.repository.mapper.ColumnInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author wx128
 * @createAt 2024/12/18
 */
@Repository
public class ColumnDao extends ServiceImpl<ColumnInfoMapper, ColumnInfoDO> {

    @Autowired
    private ColumnArticleMapper columnArticleMapper;

    /**
     * 分页查询专辑列表
     *
     * @param pageParam
     * @return
     */
    public List<ColumnInfoDO> listOnlineColumns(PageParam pageParam) {
        LambdaQueryWrapper<ColumnInfoDO> query = Wrappers.lambdaQuery();
        query.gt(ColumnInfoDO::getState, ColumnStatusEnum.OFFLINE.getCode())
                .last(PageParam.getLimitSql(pageParam))
                .orderByAsc(ColumnInfoDO::getSection);
        return baseMapper.selectList(query);
    }

    /**
     * 统计专栏的文章数
     *
     * @return
     */
    public int countColumnArticles(Long columnId) {
        LambdaQueryWrapper<ColumnArticleDO> query = Wrappers.lambdaQuery();
        if (columnId != null && columnId > 0) {
            query.eq(ColumnArticleDO::getColumnId, columnId);
        }
        return columnArticleMapper.selectCount(query).intValue();
    }

    /**
     * 获取文章列表
     *
     * @param columnId
     * @return
     */
    public List<Long> listColumnArticles(Long columnId) {
        return columnArticleMapper.listColumnArticles(columnId);
    }

    public Long getColumnArticleId(long columnId, Integer section) {
        return columnArticleMapper.getColumnArticle(columnId, section);
    }

    /**
     * 分页查询专辑列表（后台）
     *
     * @param pageParam
     * @return
     */
    public List<ColumnInfoDO> listColumns(PageParam pageParam) {
        LambdaQueryWrapper<ColumnInfoDO> query = Wrappers.lambdaQuery();
        query.last(PageParam.getLimitSql(pageParam))
                .orderByAsc(ColumnInfoDO::getSection);
        return baseMapper.selectList(query);
    }

    /**
     * 查询专辑列表总数（后台）
     *
     * @return
     */
    public Integer countColumns() {
        return lambdaQuery().count().intValue();
    }

    /**
     * 删除专栏
     *
     * @param columnId
     */
    public void deleteColumn(Integer columnId) {
        ColumnInfoDO columnInfoDO = baseMapper.selectById(columnId);
        if (columnInfoDO != null) {
            LambdaQueryWrapper<ColumnArticleDO> query = Wrappers.lambdaQuery();
            query.eq(ColumnArticleDO::getColumnId, columnId);
            columnArticleMapper.delete(query);
            baseMapper.deleteById(columnId);
        }
    }

    public int countColumnReadPeoples(Long columnId) {
        return columnArticleMapper.countColumnReadUserNums(columnId).intValue();
    }

    public List<ColumnArticleDO> listColumnArticlesDetail(Long columnId, PageParam pageParam) {
        LambdaQueryWrapper<ColumnArticleDO> query = Wrappers.lambdaQuery();
        if (columnId != null && columnId > 0) {
            query.eq(ColumnArticleDO::getColumnId, columnId);
        }
        query.orderByAsc(ColumnArticleDO::getColumnId, ColumnArticleDO::getSection);
        query.last(PageParam.getLimitSql(pageParam));
        return columnArticleMapper.selectList(query);
    }

}
