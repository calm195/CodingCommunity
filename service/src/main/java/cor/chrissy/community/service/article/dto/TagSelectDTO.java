package cor.chrissy.community.service.article.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 通用标签选择
 *
 * @author wx128
 * @createAt 2024/12/13
 */
@Data
public class TagSelectDTO implements Serializable {

    /**
     * 类型
     */
    private String selectType;

    /**
     * 描述
     */
    private String selectDesc;

    /**
     * 是否选中
     */
    private Boolean selected;
}
