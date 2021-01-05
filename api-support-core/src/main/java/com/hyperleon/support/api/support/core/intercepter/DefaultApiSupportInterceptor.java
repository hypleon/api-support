package com.hyperleon.support.api.support.core.intercepter;

import com.hyperleon.support.api.support.common.utils.ApiSignUtils;
import java.io.IOException;

/**
 * @author leon
 * @date 2020-08-11 10:15
 **/
public class DefaultApiSupportInterceptor extends AbstractApiSupportInterceptor {
    @Override
    public boolean handle(RequestInfo requestInfo) {
        return ApiSignUtils.decrypt(requestInfo);
    }
}
