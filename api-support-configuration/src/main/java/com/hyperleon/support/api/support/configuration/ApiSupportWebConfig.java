package com.hyperleon.support.api.support.configuration;

import com.hyperleon.support.api.support.core.properties.ApiSupportProperties;
import com.hyperleon.support.api.support.core.filter.WrapperFilter;
import com.hyperleon.support.api.support.core.intercepter.AbstractApiSupportInterceptor;
import com.hyperleon.support.api.support.core.intercepter.DefaultApiSupportInterceptor;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author leon
 * @date 2020-08-10 22:55
 **/
@Configuration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@ConditionalOnProperty(name = "hyper-leon.api-support.enable",havingValue = "true")
@EnableConfigurationProperties(ApiSupportProperties.class)
@SpringBootApplication
public class ApiSupportWebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(apiSupportInterceptor()).addPathPatterns("/**");
    }

    @Bean
    @ConditionalOnMissingBean(AbstractApiSupportInterceptor.class)
    public AbstractApiSupportInterceptor apiSupportInterceptor() {
        return new DefaultApiSupportInterceptor();
    }

    @Bean
    public FilterRegistrationBean wrapperFilter() {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean(new WrapperFilter());
        registrationBean.addUrlPatterns("/*");
        registrationBean.setOrder(1);
        return registrationBean;
    }

}
