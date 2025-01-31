package cor.chrissy.community.web.front.home;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import cor.chrissy.community.service.sitemap.model.SiteMap;
import cor.chrissy.community.service.sitemap.service.SitemapService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.nio.charset.Charset;

/**
 * @author wx128
 * @createAt 2025/1/14
 */
@RestController
public class SiteMapController {
    private XmlMapper xmlMapper = new XmlMapper();
    @Resource
    private SitemapService sitemapService;

    @RequestMapping(path = "/sitemap",
            produces = "application/xml;charset=utf-8")
    public SiteMap sitemap() {
        return sitemapService.getSiteMap();
    }

    @RequestMapping(path = "/sitemap.xml",
            produces = "text/xml")
    public byte[] sitemapXml() throws JsonProcessingException {
        SiteMap vo = sitemapService.getSiteMap();
        String ans = xmlMapper.writeValueAsString(vo);
        return ans.getBytes(Charset.defaultCharset());
    }

    @GetMapping(path = "/sitemap/refresh")
    public Boolean refresh() {
        sitemapService.refreshSitemap();
        return true;
    }
}

