package cor.chrissy.community.test.proxy;

/**
 * @author YiHui
 * @date 2023/2/28
 */
public class DemoService {

    public String showHello(String arg) {
        System.out.println("in function!");
        System.out.println("before return:" + arg);
        return "prefix_" + arg;
    }
}
