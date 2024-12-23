package cor.chrissy.community.web.front.home;

import cor.chrissy.community.web.front.home.helper.IndexRecommendHelper;
import cor.chrissy.community.web.front.home.vo.IndexVo;
import cor.chrissy.community.web.global.BaseViewController;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

/**
 * @author wx128
 * @createAt 2024/12/19
 */
@Controller
public class IndexController extends BaseViewController {
    @Autowired
    private IndexRecommendHelper indexRecommendHelper;


    @GetMapping(path = {"/", "", "/index"})
    public String index(Model model, HttpServletRequest request) {
        String activeTab = request.getParameter("category");
        IndexVo vo = indexRecommendHelper.buildIndexVo(activeTab);
        model.addAttribute("vo", vo);
        return "index";
    }

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
        return "biz/article/search";
    }
}

