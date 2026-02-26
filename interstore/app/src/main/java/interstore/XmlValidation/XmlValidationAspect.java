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
            "execution(org.springframework.http.ResponseEntity *(..))")
    public Object validateXmlResponse(ProceedingJoinPoint joinPoint) throws Throwable {

        Object result = joinPoint.proceed();

        if (result instanceof ResponseEntity) {
            ResponseEntity<?> responseEntity = (ResponseEntity<?>) result;
            Object body = responseEntity.getBody();

            if (body instanceof String && responseEntity.getStatusCode().is2xxSuccessful()) {
                String xmlResponse = (String) body;

                MethodSignature signature = (MethodSignature) joinPoint.getSignature();
                Method method = signature.getMethod();

                String endpoint = null;
                String httpMethod = null;

                GetMapping getMapping = method.getAnnotation(GetMapping.class);
                if (getMapping != null && getMapping.value().length > 0) {
                    endpoint = getMapping.value()[0];
                    httpMethod = "GET";
                }

                PutMapping putMapping = method.getAnnotation(PutMapping.class);
                if (putMapping != null && putMapping.value().length > 0) {
                    endpoint = putMapping.value()[0];
                    httpMethod = "PUT";
                }

                if (endpoint != null) {
                    xmlValidationService.validateXml(endpoint, httpMethod, "", xmlResponse);
                }
            }
        }

        return result;
    }
}
