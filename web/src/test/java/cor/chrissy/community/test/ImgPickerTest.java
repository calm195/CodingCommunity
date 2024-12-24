package cor.chrissy.community.test;

import cor.chrissy.community.core.util.MdImgLoader;
import org.junit.Test;

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

}
