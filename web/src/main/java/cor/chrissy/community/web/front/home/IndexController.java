package cor.chrissy.community.web.front.home;

import cor.chrissy.community.web.front.home.helper.IndexRecommendHelper;
import cor.chrissy.community.web.front.home.vo.IndexVo;
import cor.chrissy.community.web.global.BaseViewController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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
        return "views/home/index";
    }
}

