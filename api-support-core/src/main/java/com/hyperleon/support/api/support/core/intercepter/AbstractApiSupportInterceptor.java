package com.hyperleon.support.api.support.core.intercepter;

import com.hyperleon.support.api.support.common.annotation.ApiVerify;
import com.hyperleon.support.api.support.core.properties.ApiSupportProperties;
import com.hyperleon.support.api.support.core.wrapper.RequestWrapper;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * @author leon
 * @date 2020-08-11 00:38
 **/
public abstract class AbstractApiSupportInterceptor extends HandlerInterceptorAdapter {

    /**
     * handle request
     * @param requestInfo
     * @return
     * @throws IOException
     */
    public abstract boolean handle(RequestInfo requestInfo) throws IOException;

    @Autowired
    private ApiSupportProperties apiSupportProperties;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        if(Boolean.TRUE.toString().equals(apiSupportProperties.getEnableVerifyControl())) {
            ApiVerify apiVerify = handlerMethod.getMethodAnnotation(ApiVerify.class);
            if (apiVerify == null || !apiVerify.verify()) {
                return true;
            }
        }
        String signature = request.getHeader(apiSupportProperties.getApiSupportHeader());
        String uri = request.getRequestURI();
        String method = request.getMethod();
        String queryParams = request.getQueryString();
        String bodyStr = "";
        RequestWrapper wrapper = new RequestWrapper(request);
        bodyStr = wrapper.getBody();
        RequestInfo build = RequestInfo.builder()
                .uri(uri)
                .method(method)
                .queryParams(queryParams)
                .bodyStr(bodyStr)
                .timeStamp(LocalDateTime.now().toEpochSecond(ZoneOffset.of("+8")))
                .signature(signature)
                .build();
        return handle(build);
    }

    @Data
    @Builder
    @ToString
    public static class RequestInfo {
        private String uri;
        private String method;
        private String queryParams;
        private String bodyStr;
        private long timeStamp;
        private String signature;
    }


    private static class RequestBodyHelper {
        public static String getBodyStr(HttpServletRequest request) throws IOException {
            StringBuilder bodyStrBuilder = new StringBuilder();
            ServletInputStream inputStream = null;
            BufferedReader reader = null;
            inputStream = request.getInputStream();
            inputStream.mark(0);
            reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            String line = "";
            while ((line = reader.readLine()) != null) {
                bodyStrBuilder.append(line);
            }
            inputStream.reset();
            return bodyStrBuilder.toString();
        }
    }
}
