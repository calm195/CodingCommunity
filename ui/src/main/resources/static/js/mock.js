$('#getToken').click(function () {
    $.ajax({
        method: 'POST',
        url: "/wx/callback",
        contentType: 'application/xml',
        data: "<xml><URL><![CDATA[https://hhui.top]]></URL><ToUserName><![CDATA[一灰灰blog]]></ToUserName><FromUserName><![CDATA[demoUser1234]]></FromUserName><CreateTime>1655700579</CreateTime><MsgType><![CDATA[text]]></MsgType><Content><![CDATA[login]]></Content><MsgId>11111111</MsgId></xml>",
        success: function (data) {
            console.log("data", data);
            if (!data) {
                toastr.error(data.message);
            } else {
                $('#testOutput').html("<bold>" + $(data).find("Content").text() + "</bold>");
            }
        },
        error: function (data) {
            toastr.error(data);
        }
    });
})

$('#mockLogin').click(function () {
    console.log("mock login!");
    const code = this.dataset.verifyCode;
    $.ajax({
        method: 'POST',
        url: "/wx/callback",
        contentType: 'application/xml',
        data: "<xml><URL><![CDATA[https://hhui.top]]></URL><ToUserName><![CDATA[一灰灰blog]]></ToUserName><FromUserName><![CDATA[demoUser1234]]></FromUserName><CreateTime>1655700579</CreateTime><MsgType><![CDATA[text]]></MsgType><Content><![CDATA[" + code + "]]></Content><MsgId>11111111</MsgId></xml>",
        success: function (data) {
            console.log("data", data);
            if (!data) {
                toastr.error(data.message);
            }
        },
        error: function (data) {
            toastr.error(data);
        }
    });
})