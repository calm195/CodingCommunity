package cor.chrissy.community.service.article.dto;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author wx128
 * @createAt 2024/12/17
 */
@Data
public class SimpleArticleDTO implements Serializable {
    private static final long serialVersionUID = 3646376715620165839L;

    private Long id;

    private String title;

    private Timestamp createTime;
}
