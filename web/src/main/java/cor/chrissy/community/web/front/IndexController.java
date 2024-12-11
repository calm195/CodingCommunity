package cor.chrissy.community.web.front;

import cor.chrissy.community.common.req.PageParam;
import cor.chrissy.community.core.util.MapUtil;
import cor.chrissy.community.service.article.dto.ArticleListDTO;
import cor.chrissy.community.service.article.dto.CategoryDTO;
import cor.chrissy.community.service.article.service.ArticleService;
import cor.chrissy.community.service.article.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author wx128
 * @createAt 2024/12/9
 */
@Controller
public class IndexController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ArticleService articleService;

    @GetMapping(path = {"/", "", "/index"})
    public String index(Model model, HttpServletRequest request) {
        String activeTab = request.getParameter("type");
        Long categoryId = categories(model, activeTab);
        articleList(model, request, categoryId);
        homeCarouselList(model);
        sideBarItems(model);
        model.addAttribute("currentDomain", "article");
        return "index";
    }

    /**
     * 返回分类列表
     *
     * @param active
     * @return
     */
    private Long categories(Model model, String active) {
        List<CategoryDTO> list = categoryService.loadAllCategories(false);
        list.add(0, new CategoryDTO(0L, CategoryDTO.DEFAULT_TOTAL_CATEGORY, false));
        Long selectCategoryId = null;
        for (CategoryDTO c : list) {
            if (c.getCategory().equalsIgnoreCase(active)) {
                selectCategoryId = c.getCategoryId();
                c.setSelected(true);
            } else {
                c.setSelected(false);
            }
        }

        if (selectCategoryId == null) {
            // 未匹配时，默认选全部
            list.get(0).setSelected(true);
        }
        model.addAttribute("categories", list);
        return selectCategoryId;
    }


    /**
     * 文章列表
     *
     * @param model
     * @param request
     * @param categoryId
     */
    private void articleList(Model model, HttpServletRequest request, Long categoryId) {
        AtomicReference<Long> page = new AtomicReference<>(1L);
        AtomicReference<Long> pageNum = new AtomicReference<>(20L);
        Optional.ofNullable(request.getParameter("page")).ifPresent(p -> page.set(Long.parseLong(p)));
        Optional.ofNullable(request.getParameter("size")).ifPresent(p -> pageNum.set(Long.parseLong(p)));
        ArticleListDTO list = articleService.queryArticlesByCategory(categoryId, PageParam.newPageInstance(page.get(), pageNum.get()));
        model.addAttribute("articles", list);
    }

    /**
     * 轮播图
     *
     * @return
     */
    private void homeCarouselList(Model model) {
        List<Map<String, Object>> list = new ArrayList<>();
        list.add(MapUtil.createHashMap("imgUrl", "https://spring.hhui.top/spring-blog/imgs/220425/logo.jpg", "name", "spring社区", "actionUrl", "https://spring.hhui.top/"));
        list.add(MapUtil.createHashMap("imgUrl", "https://spring.hhui.top/spring-blog/imgs/220422/logo.jpg", "name", "一灰灰", "actionUrl", "https://blog.hhui.top/"));
        model.addAttribute("homeCarouselList", list);
    }


    /**
     * 侧边栏信息
     * <p>
     * fixme: 后续调整为由运营推广模块返回
     *
     * @return
     */
    private void sideBarItems(Model model) {
        List<Map<String, Object>> res = new ArrayList<>();
        res.add(MapUtil.createHashMap("title", "公告", "desc", "简单的公告内容"));
        res.add(MapUtil.createHashMap("title", "标签云", "desc", "java, web, html"));
        model.addAttribute("sideBarItems", res);
    }
}
