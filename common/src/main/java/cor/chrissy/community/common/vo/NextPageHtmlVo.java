package cor.chrissy.community.common.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author wx128
 * @createAt 2024/12/17
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NextPageHtmlVo implements Serializable {
    private String html;
    private Boolean hasMore;
}
