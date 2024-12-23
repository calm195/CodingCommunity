package cor.chrissy.community.web.global;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author wx128
 * @createAt 2024/12/19
 */
public class BaseViewController {
    @Autowired
    protected GlobalInitService globalInitService;
//
//  推荐使用它替代 GlobalViewInterceptor 中的全局属性设置
//    /**
//     * 全局属性配置
//     *
//     * @param model
//     */
//    @ModelAttribute
//    public void globalAttr(Model model) {
//        model.addAttribute("global", globalInitService.globalAttr());
//    }
}
