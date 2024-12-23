package cor.chrissy.community.web.front.article.vo;

import cor.chrissy.community.common.vo.PageListVo;
import cor.chrissy.community.service.article.dto.ColumnDTO;
import cor.chrissy.community.service.sidebar.dto.SideBarDTO;
import lombok.Data;

import java.util.List;

/**
 * @author wx128
 * @createAt 2024/12/19
 */
@Data
public class ColumnVo {
    /**
     * 专栏列表
     */
    private PageListVo<ColumnDTO> columns;

    /**
     * 侧边栏信息
     */
    private List<SideBarDTO> sideBarItems;

}

