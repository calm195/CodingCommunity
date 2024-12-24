package cor.chrissy.community.service.config.service;

import cor.chrissy.community.common.req.PageParam;
import cor.chrissy.community.common.req.banner.ConfigReq;
import cor.chrissy.community.common.vo.PageVo;
import cor.chrissy.community.service.config.dto.ConfigDTO;

/**
 * @author wx128
 * @createAt 2024/12/18
 */
public interface ConfigSettingService {
    /**
     * 保存
     *
     * @param configReq
     */
    void saveConfig(ConfigReq configReq);

    /**
     * 删除
     *
     * @param configId
     */
    void deleteConfig(Integer configId);

    /**
     * 操作（上线/下线）
     *
     * @param configId
     */
    void operateConfig(Integer configId, Integer pushStatus);

    /**
     * 获取 Banner 列表
     *
     * @param pageParam
     * @return
     */
    PageVo<ConfigDTO> getConfigList(PageParam pageParam);

    /**
     * 获取公告列表
     *
     * @param pageParam
     * @return
     */
    PageVo<ConfigDTO> getNoticeList(PageParam pageParam);
}
