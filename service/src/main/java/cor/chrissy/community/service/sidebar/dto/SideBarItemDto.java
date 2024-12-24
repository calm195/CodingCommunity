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
public class SideBarItemDto {

    private String title;

    private String name;

    private String url;

    private Long time;

    private String img;

    private List<Integer> tags;
}
