package cor.chrissy.community.service.article.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author wx128
 * @createAt 2024/12/11
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TagDTO implements Serializable {
    private static final long serialVersionUID = -8614833588325787479L;

    private Long categoryId;

    private Long tagId;

    private String tag;

    public TagDTO(Long tagId) {
        this.tagId = tagId;
    }
}
