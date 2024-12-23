package cor.chrissy.community.service.article.service.impl;

import cor.chrissy.community.service.article.dto.TagDTO;
import cor.chrissy.community.service.article.repository.dao.TagDao;
import cor.chrissy.community.service.article.service.TagService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author wx128
 * @createAt 2024/12/18
 */
@Service
public class TagServiceImpl implements TagService {
    private final TagDao tagDao;

    public TagServiceImpl(TagDao tagDao) {
        this.tagDao = tagDao;
    }

    @Override
    public List<TagDTO> queryTagsByCategoryId(Long categoryId) {
        return tagDao.listTagsByCategoryId(categoryId);
    }
}
