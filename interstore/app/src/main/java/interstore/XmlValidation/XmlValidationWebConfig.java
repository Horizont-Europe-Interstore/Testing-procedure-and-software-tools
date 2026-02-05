package interstore.XmlValidation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class XmlValidationWebConfig implements WebMvcConfigurer {

    @Autowired
    private XmlValidationInterceptor xmlValidationInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // Exclude polling endpoints (/dcap, /tm) from validation
        registry.addInterceptor(xmlValidationInterceptor)
                .addPathPatterns("/der/**", "/edev/**", "/fsa/**", "/sdev/**", "/upt/**", "/dr/**", "/msg/**", "/bill/**", "/ppy/**")
                .excludePathPatterns("/dcap", "/tm");
    }

    @Bean
    public FilterRegistrationBean<ContentCachingFilter> contentCachingFilter() {
        FilterRegistrationBean<ContentCachingFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new ContentCachingFilter());
        // Exclude polling endpoints from caching
        registrationBean.addUrlPatterns("/der/*", "/edev/*", "/fsa/*", "/sdev/*", "/upt/*", "/dr/*", "/msg/*", "/bill/*", "/ppy/*");
        registrationBean.setOrder(1);
        return registrationBean;
    }
}