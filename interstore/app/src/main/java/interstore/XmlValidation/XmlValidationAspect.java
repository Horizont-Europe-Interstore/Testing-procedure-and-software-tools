package interstore.XmlValidation;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;

import java.lang.reflect.Method;

@Aspect
@Component
public class XmlValidationAspect {

    @Autowired
    private XmlValidationService xmlValidationService;

    @Around("(@annotation(org.springframework.web.bind.annotation.GetMapping) || " +
            "@annotation(org.springframework.web.bind.annotation.PutMapping)) && " +
            "execution(org.springframework.http.ResponseEntity<String> *(..))")
    public Object validateXmlResponse(ProceedingJoinPoint joinPoint) throws Throwable {
        
        System.out.println("=== AOP Aspect Triggered ===");
        System.out.println("Method: " + joinPoint.getSignature().getName());
        
        Object result = joinPoint.proceed();
        
        System.out.println("Result type: " + (result != null ? result.getClass().getName() : "null"));
        
        if (result instanceof ResponseEntity) {
            ResponseEntity<?> responseEntity = (ResponseEntity<?>) result;
            Object body = responseEntity.getBody();
            
            System.out.println("Body type: " + (body != null ? body.getClass().getName() : "null"));
            System.out.println("Status: " + responseEntity.getStatusCode());
            
            if (body instanceof String && responseEntity.getStatusCode().is2xxSuccessful()) {
                String xmlResponse = (String) body;
                
                System.out.println("XML Response length: " + xmlResponse.length());
                
                MethodSignature signature = (MethodSignature) joinPoint.getSignature();
                Method method = signature.getMethod();
                
                String endpoint = null;
                String httpMethod = null;
                
                GetMapping getMapping = method.getAnnotation(GetMapping.class);
                if (getMapping != null && getMapping.value().length > 0) {
                    endpoint = getMapping.value()[0];
                    httpMethod = "GET";
                    System.out.println("Found GetMapping: " + endpoint);
                }
                
                PutMapping putMapping = method.getAnnotation(PutMapping.class);
                if (putMapping != null && putMapping.value().length > 0) {
                    endpoint = putMapping.value()[0];
                    httpMethod = "PUT";
                    System.out.println("Found PutMapping: " + endpoint);
                }
                
                if (endpoint != null && httpMethod != null) {
                    System.out.println("Calling validation service for: " + endpoint);
                    try {
                        xmlValidationService.validateXml(endpoint, httpMethod, "", xmlResponse);
                        System.out.println("Validation completed");
                    } catch (Exception e) {
                        System.err.println("Validation error: " + e.getMessage());
                        e.printStackTrace();
                    }
                } else {
                    System.out.println("No endpoint mapping found");
                }
            }
        }
        
        return result;
    }
}
