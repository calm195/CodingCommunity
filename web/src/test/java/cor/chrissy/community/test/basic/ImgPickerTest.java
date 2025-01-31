package cor.chrissy.community.test.basic;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import cor.chrissy.community.common.req.user.wx.WxImgTxtItem;
import cor.chrissy.community.common.req.user.wx.WxImgTxtMsgRes;
import cor.chrissy.community.core.util.ArticleUtil;
import cor.chrissy.community.core.util.IpUtil;
import cor.chrissy.community.core.util.MdImgLoader;
import org.junit.Test;

import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author wx128
 * @createAt 2024/12/24
 */
public class ImgPickerTest {

    @Test
    public void testLoad() {
        // markdown中的图片识别
        String pattern = "!\\[(.*?)\\]\\((.*?)\\)";
        String text = "hello ![](https://text.jpg) world!![描述](http))!图片";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(text);
        while (m.find()) {
            String ans = m.group(0);
            System.out.println(ans);
        }
    }

    @Test
    public void testMdImgLoad() {
        String text = "hello ![](https://text.jpg) world!![描述](http))!图片";
        List<MdImgLoader.MdImg> list = MdImgLoader.loadImgs(text);
        System.out.println(list);
    }


    @Test
    public void testArticleSummaryGen() {
        String txt = "hello 这是要给简单的测试 ![](https://text.jpg) <br/> 这是一个超链 [我的链接](https://www.hhui.top) 哈哈哈 <b>加粗</b>见到了附件123132131!";
        String ans = ArticleUtil.pickSummary(txt);
        System.out.println(ans);
    }

    @Test
    public void test() throws SocketException {
        String ip = IpUtil.getLocalIp4Address();
        System.out.println(ip);
        System.out.println(IpUtil.getLocationByIp("121.40.134.96").toRegionStr());
    }

    @Test
    public void test2xml() throws JsonProcessingException {
        WxImgTxtMsgRes vo = new WxImgTxtMsgRes();
        vo.setFromUserName("一会");
        vo.setToUserName("dd");
        vo.setCreateTime(System.currentTimeMillis() / 1000);
        vo.setArticleCount(1);

        List<WxImgTxtItem> itemList = new ArrayList<>();
        WxImgTxtItem item = new WxImgTxtItem();
        item.setTitle("haha");
        item.setDescription("miaos");
        item.setPicUrl("123");
        item.setUrl("456");
        itemList.add(item);
        vo.setArticles(itemList);

        XmlMapper mapper = new XmlMapper();
        String ans = mapper.writeValueAsString(vo);
        System.out.println(ans);
    }
}

