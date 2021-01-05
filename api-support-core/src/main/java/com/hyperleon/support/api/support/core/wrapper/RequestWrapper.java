package com.hyperleon.support.api.support.core.wrapper;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * @author leon
 * @date  2020-08-11 00:36
 **/
@Slf4j
public class RequestWrapper extends HttpServletRequestWrapper {
    private final byte[] body;

    public RequestWrapper(HttpServletRequest request) {
        super(request);
        try {
            body = RequestBodyHelper.getBodyStr(request).getBytes(StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(body);
        return new ServletInputStream() {
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
            @Override
            public int read()  {
                return byteArrayInputStream.read();
            }
        };

    }

    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(this.getInputStream()));
    }

    public String getBody() {
        return new String(body, StandardCharsets.UTF_8);
    }


    private static class RequestBodyHelper {
        private static String getBodyStr(HttpServletRequest request) throws IOException {
            StringBuilder bodyStrBuilder = new StringBuilder();
            ServletInputStream inputStream = null;
            BufferedReader reader = null;
            try {
                inputStream = request.getInputStream();
                reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
                String line = "";
                while ((line = reader.readLine()) != null) {
                    bodyStrBuilder.append(line);
                }
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        log.error(e.getMessage());
                    }
                }
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        log.error(e.getMessage());
                    }
                }
            }
            return bodyStrBuilder.toString();
        }
    }

}
