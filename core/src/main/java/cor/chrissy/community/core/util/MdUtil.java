package cor.chrissy.community.core.util;

import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Document;

/**
 * @author wx128
 * @createAt 2025/1/11
 */
public class MdUtil {
    public static String mdToHtml(String str) {
        Parser parser = Parser.builder().build();
        Document document = parser.parse(str);
        HtmlRenderer renderer = HtmlRenderer.builder().build();
        return renderer.render(document);
    }
}
