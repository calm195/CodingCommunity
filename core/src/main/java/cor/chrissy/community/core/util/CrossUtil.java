package cor.chrissy.community.core.util;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 跨域工具类
 *
 * @author wx128
 * @createAt 2024/12/9
 */
public class CrossUtil {
    /**
     * 支持跨域
     *
     * @param req  请求体
     * @param resp 返回体
     */
    public static void buildCors(HttpServletRequest req, HttpServletResponse resp) {
        String origin = req.getHeader("Origin");
        if (StringUtils.isBlank(origin)) {
            resp.setHeader("Access-Control-Allow-Origin", "*");
        } else {
            resp.setHeader("Access-Control-Allow-Origin", origin);
        }
        resp.setHeader("Access-Control-Allow-Origin", req.getHeader("Origin"));
        resp.setHeader("Access-Control-Allow-Credentials", "true");
        resp.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, PUT, DELETE, HEAD");
        resp.setHeader("Access-Control-Max-Age", "3600");
        resp.setHeader("Access-Control-Allow-Headers",
                "Origin, X-Requested-With, Content-Type, Accept, X-Real-IP, X-Forwarded-For, d-uuid, User-Agent, x-zd-cs, Proxy-Client-IP, HTTP_CLIENT_IP, HTTP_X_FORWARDED_FOR");
    }
}
