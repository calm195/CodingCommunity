package cor.chrissy.community.common.req.user.wx;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;

/**
 * 微信官方提供的请求体参数，用于与公众号后台交互
 *
 * @link <a href="https://developers.weixin.qq.com/doc/offiaccount/Message_Management/Receiving_standard_messages.html"/>
 *
 * @author wx128
 * @createAt 2024/12/13
 */
@Data
@JacksonXmlRootElement(localName = "xml")
public class WxTxtMsgReq {
    @JacksonXmlProperty(localName = "ToUserName")
    private String toUserName;
    @JacksonXmlProperty(localName = "FromUserName")
    private String fromUserName;
    @JacksonXmlProperty(localName = "CreateTime")
    private Long createTime;
    @JacksonXmlProperty(localName = "MsgType")
    private String msgType;
    @JacksonXmlProperty(localName = "Content")
    private String content;
    @JacksonXmlProperty(localName = "MsgId")
    private String msgId;
    @JacksonXmlProperty(localName = "MsgDataId")
    private String msgDataId;
    @JacksonXmlProperty(localName = "Idx")
    private String idx;
}