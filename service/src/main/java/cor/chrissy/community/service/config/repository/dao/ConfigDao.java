package cor.chrissy.community.service.config.repository.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cor.chrissy.community.common.enums.ConfigTypeEnum;
import cor.chrissy.community.common.enums.PushStatEnum;
import cor.chrissy.community.common.enums.YesOrNoEnum;
import cor.chrissy.community.common.req.PageParam;
import cor.chrissy.community.service.config.converter.ConfigConverter;
import cor.chrissy.community.service.config.dto.ConfigDTO;
import cor.chrissy.community.service.config.repository.entity.ConfigDO;
import cor.chrissy.community.service.config.repository.mapper.ConfigMapper;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wx128
 * @createAt 2024/12/24
 */
@Repository
public class ConfigDao extends ServiceImpl<ConfigMapper, ConfigDO> {

    /**
     * 根据类型获取配置列表（无需分页）
     *
     * @param type
     * @return
     */
    public List<ConfigDTO> listConfigByType(Integer type) {
        List<ConfigDO> configDOS = lambdaQuery()
                .eq(ConfigDO::getType, type)
                .eq(ConfigDO::getStatus, PushStatEnum.ONLINE.getCode())
                .eq(ConfigDO::getDeleted, YesOrNoEnum.NO.getCode())
                .orderByAsc(ConfigDO::getRank)
                .list();
        return ConfigConverter.toDTOS(configDOS);
    }

    /**
     * 获取所有 Banner 列表（分页）
     *
     * @return
     */
    public List<ConfigDTO> listBanner(PageParam pageParam) {
        List<Integer> typeList = new ArrayList<>();
        typeList.add(ConfigTypeEnum.HOME_PAGE.getCode());
        typeList.add(ConfigTypeEnum.SIDE_PAGE.getCode());
        typeList.add(ConfigTypeEnum.ADVERTISEMENT.getCode());
        typeList.add(ConfigTypeEnum.NOTICE.getCode());
        typeList.add(ConfigTypeEnum.COLUMN.getCode());
        typeList.add(ConfigTypeEnum.PDF.getCode());

        List<ConfigDO> configDOS = lambdaQuery()
                .in(ConfigDO::getType, typeList)
                .eq(ConfigDO::getDeleted, YesOrNoEnum.NO.getCode())
                .orderByDesc(ConfigDO::getCreateTime)
                .last(PageParam.getLimitSql(pageParam))
                .list();
        return ConfigConverter.toDTOS(configDOS);
    }

    /**
     * 获取所有 Banner 总数（分页）
     *
     * @return
     */
    public Integer countConfig() {
        List<Integer> typeList = new ArrayList<>();
        typeList.add(ConfigTypeEnum.HOME_PAGE.getCode());
        typeList.add(ConfigTypeEnum.SIDE_PAGE.getCode());
        typeList.add(ConfigTypeEnum.ADVERTISEMENT.getCode());
        typeList.add(ConfigTypeEnum.NOTICE.getCode());
        typeList.add(ConfigTypeEnum.COLUMN.getCode());
        typeList.add(ConfigTypeEnum.PDF.getCode());

        return lambdaQuery()
                .in(ConfigDO::getType, typeList)
                .eq(ConfigDO::getDeleted, YesOrNoEnum.NO.getCode())
                .count()
                .intValue();
    }

    /**
     * 获取所有公告列表（分页）
     *
     * @return
     */
    public List<ConfigDTO> listNotice(PageParam pageParam) {
        List<ConfigDO> configDOS = lambdaQuery()
                .eq(ConfigDO::getType, ConfigTypeEnum.NOTICE.getCode())
                .eq(ConfigDO::getDeleted, YesOrNoEnum.NO.getCode())
                .orderByDesc(ConfigDO::getCreateTime)
                .last(PageParam.getLimitSql(pageParam))
                .list();
        return ConfigConverter.toDTOS(configDOS);
    }

    /**
     * 获取所有公告总数（分页）
     *
     * @return
     */
    public Integer countNotice() {
        return lambdaQuery()
                .eq(ConfigDO::getType, ConfigTypeEnum.NOTICE.getCode())
                .eq(ConfigDO::getDeleted, YesOrNoEnum.NO.getCode())
                .count()
                .intValue();
    }
}
