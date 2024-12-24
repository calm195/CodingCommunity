package cor.chrissy.community.web.front.search.view;

import cor.chrissy.community.web.front.home.helper.IndexRecommendHelper;
import cor.chrissy.community.web.front.home.vo.IndexVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author wx128
 * @createAt 2024/12/24
 */
@Controller
public class SearchViewController {

    @Autowired
    private IndexRecommendHelper indexRecommendHelper;

    /**
     * 查询文章列表
     *
     * @param model
     */
    @GetMapping(path = "search")
    public String searchArticleList(@RequestParam(name = "key") String key, Model model) {
        if (!StringUtils.isBlank(key)) {
            IndexVo vo = indexRecommendHelper.buildSearchVo(key);
            model.addAttribute("vo", vo);
        }
        return "views/article-search-list/index";
    }
}

