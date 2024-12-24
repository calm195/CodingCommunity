package cor.chrissy.community.service.article.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author wx128
 * @createAt 2024/12/23
 */
@Data
public class DictCommonDTO implements Serializable {
    private static final long serialVersionUID = -8614833588325787479L;

    private String typeCode;

    private String dictCode;

    private String dictDesc;

    private Integer sortNo;
}
