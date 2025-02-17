package cor.chrissy.community.web.hook.filter;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

/**
 * post流数据封装，避免因日志导致参数被消费
 *
 * @author wx128
 * @createAt 2024/12/9
 */
public class BodyReaderHttpServletRequestWrapper extends HttpServletRequestWrapper {
    private static final List<String> POST_METHOD = Arrays.asList("POST", "PUT");
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final byte[] body;
    @Getter
    private final String bodyString;

    public BodyReaderHttpServletRequestWrapper(HttpServletRequest request) {
        super(request);

        if (POST_METHOD.contains(request.getMethod()) && !isMultipart(request) && !isBinaryContent(request) && !isFromPost(request)) {
            bodyString = getBodyString(request);
            body = bodyString.getBytes(StandardCharsets.UTF_8);
        } else {
            bodyString = null;
            body = null;
        }
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(getInputStream()));
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        if (body == null) {
            return super.getInputStream();
        }

        final ByteArrayInputStream bodies = new ByteArrayInputStream(body);
        return new ServletInputStream() {
            @Override
            public int read() throws IOException {
                return bodies.read();
            }

            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener readListener) {
            }
        };
    }

    private String getBodyString(HttpServletRequest request) {
        BufferedReader br;
        try {
            br = request.getReader();
        } catch (IOException e) {
            logger.warn("Failed to get reader", e);
            return "";
        }

        String str;
        StringBuilder body = new StringBuilder();
        try {
            while ((str = br.readLine()) != null) {
                body.append(str);
            }
        } catch (IOException e) {
            logger.warn("Failed to read line", e);
        }

        try {
            br.close();
        } catch (IOException e) {
            logger.warn("Failed to close reader", e);
        }

        return body.toString();
    }

    private boolean isBinaryContent(final HttpServletRequest request) {
        return request.getContentType() != null &&
                (request.getContentType().startsWith("image") || request.getContentType().startsWith("video") ||
                        request.getContentType().startsWith("audio"));
    }

    private boolean isMultipart(final HttpServletRequest request) {
        return request.getContentType() != null && request.getContentType().startsWith("multipart/form-data");
    }

    private boolean isFromPost(final HttpServletRequest request) {
        return request.getContentType() != null && request.getContentType().startsWith("application/x-www-form-urlencoded");
    }
}
