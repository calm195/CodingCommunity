package cor.chrissy.community.service.article.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author wx128
 * @createAt 2024/12/11
 */
@Data
public class TagDTO implements Serializable {
    private static final long serialVersionUID = -8614833588325787479L;

    private Long categoryId;

    private String categoryName;

    private Long tagId;

    private String tag;

    private Integer status;

    private Boolean selected;
}
