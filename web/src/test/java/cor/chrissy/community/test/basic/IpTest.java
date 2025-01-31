package cor.chrissy.community.test.basic;

import cor.chrissy.community.core.util.IpUtil;
import org.junit.Test;

import java.net.SocketException;

/**
 * @author YiHui
 * @date 2023/2/14
 */
public class IpTest {

    @Test
    public void test() throws SocketException {
        String ip = IpUtil.getLocalIp4Address();
        System.out.println(ip);
        System.out.println(IpUtil.getLocationByIp("121.40.134.96").toRegionStr());
    }
}
