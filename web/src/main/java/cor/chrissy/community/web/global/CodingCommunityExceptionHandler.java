package cor.chrissy.community.web.global;

import cor.chrissy.community.common.exception.CommunityException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author wx128
 * @createAt 2024/12/18
 */
@Slf4j
public class CodingCommunityExceptionHandler implements HandlerExceptionResolver {
    @Override
    public ModelAndView resolveException(HttpServletRequest request,
                                         HttpServletResponse response,
                                         Object handler, Exception ex) {

        log.error("unexpect error", ex);
        ModelAndView mv = new ModelAndView("error/500");
        if (ex instanceof CommunityException) {
            mv.getModel().put("toast", ((CommunityException) ex).getStatus().getMessage());
        } else {
            mv.getModel().put("toast", ExceptionUtils.getStackTrace(ex));
        }
        response.setStatus(500);
        return mv;
    }
}
