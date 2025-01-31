package cor.chrissy.community.service.sitemap.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wx128
 * @createAt 2025/1/14
 */
@Data
@JacksonXmlRootElement(localName = "urlset", namespace = "http://www.sitemaps.org/schemas/sitemap/0.9")
public class SiteMap {
    /**
     * 将列表数据转为XML节点， useWrapping = false 表示不要外围标签名
     */
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "url")
    private List<SiteUrl> url;

    public SiteMap() {
        url = new ArrayList<>();
    }

    public void addUrl(SiteUrl xmlUrl) {
        url.add(xmlUrl);
    }
}
