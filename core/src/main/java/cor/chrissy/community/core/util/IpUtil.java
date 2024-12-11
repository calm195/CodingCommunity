package cor.chrissy.community.core.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * IP工具类
 * @author wx128
 * @createAt 2024/12/9
 */
@Slf4j
public class IpUtil {
    private static final String UNKNOWN = "unKnown";

    /**
     * 获取客户端真实IP。发生错误则返回 "x.0.0.1"
     * @param req 请求体
     * @return IP字符串
     */
    public static String getClientIp(HttpServletRequest req) {
        try {
            String xIp = req.getHeader("X-Real-IP");
            String xFor = req.getHeader("X-Forwarded-For");
            if (StringUtils.isNotEmpty(xFor) && !UNKNOWN.equalsIgnoreCase(xFor)) {
                //多次反向代理后会有多个ip值，第一个ip才是真实ip
                int index = xFor.indexOf(",");
                if (index != -1) {
                    return xFor.substring(0, index);
                } else {
                    return xFor;
                }
            }
            xFor = xIp;
            if (StringUtils.isNotEmpty(xFor) && !UNKNOWN.equalsIgnoreCase(xFor)) {
                return xFor;
            }
            if (StringUtils.isBlank(xFor) || UNKNOWN.equalsIgnoreCase(xFor)) {
                xFor = req.getHeader("Proxy-Client-IP");
            }
            if (StringUtils.isBlank(xFor) || UNKNOWN.equalsIgnoreCase(xFor)) {
                xFor = req.getHeader("WL-Proxy-Client-IP");
            }
            if (StringUtils.isBlank(xFor) || UNKNOWN.equalsIgnoreCase(xFor)) {
                xFor = req.getHeader("HTTP_CLIENT_IP");
            }
            if (StringUtils.isBlank(xFor) || UNKNOWN.equalsIgnoreCase(xFor)) {
                xFor = req.getHeader("HTTP_X_FORWARDED_FOR");
            }
            if (StringUtils.isBlank(xFor) || UNKNOWN.equalsIgnoreCase(xFor)) {
                xFor = req.getRemoteAddr();
            }
            return xFor;
        } catch (Exception e) {
            log.error("get remote ip error!", e);
            return "x.0.0.1";
        }
    }

}
