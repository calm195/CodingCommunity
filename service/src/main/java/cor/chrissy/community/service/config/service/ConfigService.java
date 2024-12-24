package cor.chrissy.community.service.config.service;

import cor.chrissy.community.common.enums.ConfigTypeEnum;
import cor.chrissy.community.service.config.dto.ConfigDTO;

import java.util.List;

/**
 * @author wx128
 * @createAt 2024/12/18
 */
public interface ConfigService {
    /**
     * 获取 Banner 列表
     *
     * @param configTypeEnum
     * @return
     */
    List<ConfigDTO> getConfigList(ConfigTypeEnum configTypeEnum);
}
