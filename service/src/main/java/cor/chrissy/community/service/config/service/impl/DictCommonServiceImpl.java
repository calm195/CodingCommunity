package cor.chrissy.community.service.config.service.impl;

import com.google.common.collect.Maps;
import cor.chrissy.community.service.article.dto.DictCommonDTO;
import cor.chrissy.community.service.config.repository.dao.DictCommonDao;
import cor.chrissy.community.service.config.service.DictCommonService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author wx128
 * @createAt 2024/12/24
 */
@Service
public class DictCommonServiceImpl implements DictCommonService {

    @Resource
    private DictCommonDao dictCommonDao;

    @Override
    public Map<String, Object> getDict() {
        Map<String, Object> result = Maps.newLinkedHashMap();

        List<DictCommonDTO> dictCommonList = dictCommonDao.getDictList();

        Map<String, Map<String, String>> dictCommonMap = Maps.newLinkedHashMap();
        for (DictCommonDTO dictCommon : dictCommonList) {
                Map<String, String> codeMap = dictCommonMap.get(dictCommon.getTypeCode());
                if (codeMap == null || codeMap.isEmpty()) {
                    codeMap = Maps.newLinkedHashMap();
                    dictCommonMap.put(dictCommon.getTypeCode(), codeMap);
                }
                codeMap.put(dictCommon.getDictCode(), dictCommon.getDictDesc());
        }

        result.putAll(dictCommonMap);
        return result;
    }
}
