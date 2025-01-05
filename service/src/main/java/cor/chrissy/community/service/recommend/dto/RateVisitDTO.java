package cor.chrissy.community.service.recommend.dto;

import lombok.Data;

/**
 * @author wx128
 * @createAt 2025/1/2
 */
@Data
public class RateVisitDTO {

    /**
     * 查看次数
     */
    private Integer visit;

    /**
     * 下载次数
     */
    private Integer download;

    /**
     * 评分, 浮点数，string方式返回，避免精度问题
     */
    private String rate;

    public RateVisitDTO() {
        visit = 0;
        download = 0;
        rate = "8";
    }

    public void incrVisit() {
        visit += 1;
    }

    public void incrDownload() {
        download += 1;
    }
}
