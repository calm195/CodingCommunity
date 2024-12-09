package cor.chrissy.community.service.article.service.impl;

import cor.chrissy.community.service.article.repository.mapper.ArticleTagMapper;
import cor.chrissy.community.service.article.service.ArticleTagService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author wx128
 * @date 2024/12/9
 */
@Service
public class ArticleTagServiceImpl implements ArticleTagService {
    @Resource
    private ArticleTagMapper articleTagMapper;
}
