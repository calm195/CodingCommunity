package cor.chrissy.community.service.article.conveter;

import cor.chrissy.community.common.req.article.ColumnArticleReq;
import cor.chrissy.community.common.req.article.ColumnReq;
import cor.chrissy.community.service.article.dto.ColumnDTO;
import cor.chrissy.community.service.article.repository.entity.ColumnArticleDO;
import cor.chrissy.community.service.article.repository.entity.ColumnInfoDO;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author wx128
 * @createAt 2024/12/18
 */
public class ColumnConverter {

    public static ColumnDTO toDto(ColumnInfoDO info) {
        ColumnDTO dto = new ColumnDTO();
        dto.setColumnId(info.getId());
        dto.setColumn(info.getColumnName());
        dto.setCover(info.getCover());
        dto.setIntroduction(info.getIntroduction());
        dto.setState(info.getState());
        dto.setAuthor(info.getUserId());
        dto.setPublishTime(info.getPublishTime().getTime());
        dto.setNums(info.getNums());
        dto.setSection(info.getSection());
        dto.setType(info.getType());
        dto.setFreeStartTime(info.getFreeStartTime().getTime());
        dto.setFreeEndTime(info.getFreeEndTime().getTime());
        return dto;
    }

    // todo：rename
    public static List<ColumnDTO> toDtos(List<ColumnInfoDO> columnInfoDOS) {
        List<ColumnDTO> columnDTOS = new ArrayList<>();
        columnInfoDOS.forEach(info -> columnDTOS.add(ColumnConverter.toDto(info)));
        return columnDTOS;
    }

    public static ColumnInfoDO toDo(ColumnReq columnReq) {
        if (columnReq == null) {
            return null;
        }
        ColumnInfoDO columnInfoDO = new ColumnInfoDO();
        columnInfoDO.setColumnName(columnReq.getColumn());
        columnInfoDO.setUserId(columnReq.getAuthor());
        columnInfoDO.setIntroduction(columnReq.getIntroduction());
        columnInfoDO.setCover(columnReq.getCover());
        columnInfoDO.setState(columnReq.getState());
        columnInfoDO.setSection(columnReq.getSection());
        columnInfoDO.setNums(columnReq.getNums());
        columnInfoDO.setType(columnReq.getType());
        columnInfoDO.setFreeStartTime(new Date(columnReq.getFreeStartTime()));
        columnInfoDO.setFreeEndTime(new Date(columnReq.getFreeEndTime()));
        return columnInfoDO;
    }

    public static ColumnArticleDO toDo(ColumnArticleReq columnArticleReq) {
        if (columnArticleReq == null) {
            return null;
        }
        ColumnArticleDO columnArticleDO = new ColumnArticleDO();
        columnArticleDO.setColumnId(columnArticleReq.getColumnId());
        columnArticleDO.setArticleId(columnArticleReq.getArticleId());
        columnArticleDO.setSection(columnArticleReq.getSort());
        return columnArticleDO;
    }
}

