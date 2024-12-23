package cor.chrissy.community.service.sidebar.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author wx128
 * @createAt 2024/12/17
 */
@Data
@Accessors(chain = true)
public class SideBarDTO {

    private String title;

    private String subTitle;

    private String icon;

    private String img;

    private String content;

    private List<SideBarItemDto> items;

    /**
     * 侧边栏样式
     */
    private Integer style;
}
