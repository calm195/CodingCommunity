package cor.chrissy.community.service.sidebar.service;

import cor.chrissy.community.service.sidebar.dto.SideBarDTO;

import java.util.List;

/**
 * @author wx128
 * @createAt 2024/12/18
 */
public interface SidebarService {
    /**
     * 查询首页的侧边栏信息
     *
     * @return
     */
    List<SideBarDTO> queryHomeSidebarList();

    /**
     * 查询 PDF 的侧边栏
     *
     * @return
     */
    SideBarDTO pdfSideBar();
}
