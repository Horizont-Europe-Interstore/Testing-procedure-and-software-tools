package interstore.XmlValidation;

import org.springframework.context.annotation.Configuration;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class XmlValidationWebConfig implements WebMvcConfigurer {

    @Bean
    public FilterRegistrationBean<ContentCachingFilter> contentCachingFilter(XmlValidationService xmlValidationService) {
        FilterRegistrationBean<ContentCachingFilter> registrationBean = new FilterRegistrationBean<>();
        ContentCachingFilter filter = new ContentCachingFilter();
        filter.setXmlValidationService(xmlValidationService);
        registrationBean.setFilter(filter);
        registrationBean.addUrlPatterns("/dcap", "/der/**", "/edev/**", "/fsa/**", "/sdev/**", "/tm/**", "/derp/**");
        registrationBean.setOrder(1);
        return registrationBean;
    }
}