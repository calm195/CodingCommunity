package cor.chrissy.community.service.config.service.impl;

import cor.chrissy.community.common.enums.ConfigTypeEnum;
import cor.chrissy.community.service.config.dto.ConfigDTO;
import cor.chrissy.community.service.config.repository.dao.ConfigDao;
import cor.chrissy.community.service.config.service.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author wx128
 * @createAt 2024/12/18
 */
@Service
public class ConfigServiceImpl implements ConfigService {

    @Autowired
    private ConfigDao configDao;

    @Override
    public List<ConfigDTO> getConfigList(ConfigTypeEnum configTypeEnum) {
        return configDao.listConfigByType(configTypeEnum.getCode());
    }

    @Override
    public void updateVisit(long configId, String extra) {
        configDao.updatePdfConfigVisitNum(configId, extra);
    }
}
