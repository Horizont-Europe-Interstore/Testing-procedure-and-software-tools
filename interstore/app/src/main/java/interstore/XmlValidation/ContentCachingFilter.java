package interstore.XmlValidation;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
public class ContentCachingFilter extends OncePerRequestFilter {

    private XmlValidationService xmlValidationService;

    public void setXmlValidationService(XmlValidationService xmlValidationService) {
        this.xmlValidationService = xmlValidationService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, 
                                  FilterChain filterChain) throws ServletException, IOException {
        
        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);
        
        try {
            filterChain.doFilter(requestWrapper, responseWrapper);
        } finally {
            if (xmlValidationService != null) {
                validateXmlIfNeeded(requestWrapper, responseWrapper);
            }
            responseWrapper.copyBodyToResponse();
        }
    }

    private void validateXmlIfNeeded(ContentCachingRequestWrapper request, ContentCachingResponseWrapper response) {
        String contentType = response.getContentType();
        if (contentType != null && 
            (contentType.contains("application/xml") || 
             contentType.contains("text/xml") || 
             contentType.contains("application/sep+xml")) &&
            response.getStatus() == 200) {
            
            String endpoint = request.getRequestURI();
            String method = request.getMethod();
            
            String requestXml = new String(request.getContentAsByteArray(), StandardCharsets.UTF_8);
            String responseXml = new String(response.getContentAsByteArray(), StandardCharsets.UTF_8);
            
            if (responseXml != null && !responseXml.isEmpty()) {
                xmlValidationService.validateXml(endpoint, method, requestXml, responseXml);
            }
        }
    }
}