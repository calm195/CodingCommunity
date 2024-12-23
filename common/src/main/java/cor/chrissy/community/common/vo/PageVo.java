package cor.chrissy.community.common.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author wx128
 * @createAt 2024/12/17
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageVo<T> {

    private List<T> list;

    private long pageSize;

    private long pageNum;

    private long pageTotal;

    private long total;

    /**
     * 构造PageVO
     *
     * @param list
     * @param pageSize
     * @param pageNum
     * @param total
     * @return
     */
    public PageVo(List<T> list, long pageSize, long pageNum, long total) {
        this.list = list;
        this.total = total;
        this.pageSize = pageSize;
        this.pageNum = pageNum;
        this.pageTotal = (long) Math.ceil((double) total / pageSize);
    }

    /**
     * 创建PageVO
     *
     * @param list
     * @param pageSize
     * @param pageNum
     * @param total
     * @return PageVo<T>
     */
    public static <T> PageVo<T> build(List<T> list, long pageSize, long pageNum, long total) {
        return new PageVo<>(list, pageSize, pageNum, total);
    }
}

