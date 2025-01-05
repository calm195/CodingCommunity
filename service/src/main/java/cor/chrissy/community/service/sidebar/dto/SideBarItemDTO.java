package cor.chrissy.community.service.sidebar.dto;

import cor.chrissy.community.service.recommend.dto.RateVisitDTO;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author wx128
 * @createAt 2025/1/2
 */
@Data
@Accessors(chain = true)
public class SideBarItemDTO {

    private String title;

    private String name;

    private String url;

    private String img;

    private Long time;

    /**
     * tag列表
     */
    private List<Integer> tags;

    /**
     * 评分信息
     */
    private RateVisitDTO visit;
}
