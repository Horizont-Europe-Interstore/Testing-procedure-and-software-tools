package interstore.XmlValidation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;

@Component
public class XmlValidationInterceptor implements HandlerInterceptor {

    @Autowired
    private XmlValidationService xmlValidationService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, 
                              Object handler, Exception ex) throws Exception {
        
        System.out.println("=== XML Validation Interceptor ===");
        System.out.println("URI: " + request.getRequestURI());
        System.out.println("Method: " + request.getMethod());
        System.out.println("Status: " + response.getStatus());
        System.out.println("Content-Type: " + response.getContentType());
        
        if (shouldValidateResponse(request, response)) {
            String endpoint = request.getRequestURI();
            String method = request.getMethod();
            
            String requestXml = getRequestBody(request);
            String responseXml = getResponseBody(response);
            
            System.out.println("Request XML length: " + (requestXml != null ? requestXml.length() : 0));
            System.out.println("Response XML length: " + (responseXml != null ? responseXml.length() : 0));
            
            if (responseXml != null && !responseXml.isEmpty()) {
                System.out.println("Validating XML for: " + method + " " + endpoint);
                xmlValidationService.validateXml(endpoint, method, requestXml, responseXml);
            } else {
                System.out.println("No response XML to validate");
            }
        } else {
            System.out.println("Skipping validation - conditions not met");
        }
    }

    private boolean shouldValidateResponse(HttpServletRequest request, HttpServletResponse response) {
        String contentType = response.getContentType();
        return contentType != null && 
               (contentType.contains("application/xml") || 
                contentType.contains("text/xml") || 
                contentType.contains("application/sep+xml")) &&
               response.getStatus() == 200;
    }

    private String getRequestBody(HttpServletRequest request) {
        if (request instanceof ContentCachingRequestWrapper) {
            ContentCachingRequestWrapper wrapper = (ContentCachingRequestWrapper) request;
            byte[] content = wrapper.getContentAsByteArray();
            return new String(content, StandardCharsets.UTF_8);
        }
        return "";
    }

    private String getResponseBody(HttpServletResponse response) {
        if (response instanceof ContentCachingResponseWrapper) {
            ContentCachingResponseWrapper wrapper = (ContentCachingResponseWrapper) response;
            byte[] content = wrapper.getContentAsByteArray();
            return new String(content, StandardCharsets.UTF_8);
        }
        return "";
    }
}