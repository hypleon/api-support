package com.hyperleon.support.api.support.core.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author leon
 * @date  2020-08-11 15:57
 **/
@Data
@ConfigurationProperties(prefix = "hyper-leon.api-support")
@Component
public class ApiSupportProperties {
    private String enable;
    private String enableVerifyControl;
    private String apiSupportHeader;
    private String appKey;
    private String appSecret;
}
