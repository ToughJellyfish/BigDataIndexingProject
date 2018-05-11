package edu.neu.coe.info7255.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.ShallowEtagHeaderFilter;
import javax.servlet.Filter;

@Configuration
public class ETagConfig {

    @Bean
    public Filter eTagFilter(){
        return new ShallowEtagHeaderFilter();
    }

    @Bean
    FilterRegistrationBean headerLogger () {
        FilterRegistrationBean frb = new FilterRegistrationBean();
        frb.setFilter(new HeaderLogger());
        frb.addUrlPatterns("/*");
        frb.setOrder(1);
        return frb;
    }
}
