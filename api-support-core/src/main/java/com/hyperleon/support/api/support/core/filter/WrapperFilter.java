package com.hyperleon.support.api.support.core.filter;

import com.hyperleon.support.api.support.core.wrapper.RequestWrapper;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author leon
 * @date  2020-08-11 00:39
 **/
public class WrapperFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) {}

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        RequestWrapper requestWrapper = new RequestWrapper((HttpServletRequest) servletRequest);
        filterChain.doFilter(requestWrapper,servletResponse);
    }

    @Override
    public void destroy() {}
}