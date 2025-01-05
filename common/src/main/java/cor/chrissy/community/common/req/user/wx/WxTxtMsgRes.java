package cor.chrissy.community.common.req.user.wx;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 微信公众号后台返回的数据结构体
 *
 * @author wx128
 * @createAt 2024/12/13
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@JacksonXmlRootElement(localName = "xml")
public class WxTxtMsgRes extends BaseWxMsgRes {
    @JacksonXmlProperty(localName = "Content")
    private String content;

    public WxTxtMsgRes() {
        setMsgType("text");
    }
}
