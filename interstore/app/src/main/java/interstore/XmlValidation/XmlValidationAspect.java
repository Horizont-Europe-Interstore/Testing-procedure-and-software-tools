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
import java.util.logging.Logger;

@Aspect
@Component
public class XmlValidationAspect {
    private static final Logger LOGGER = Logger.getLogger(XmlValidationAspect.class.getName());

    @Autowired
    private XmlValidationService xmlValidationService;

    @Around("@annotation(org.springframework.web.bind.annotation.GetMapping) && " +
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

                GetMapping getMapping = method.getAnnotation(GetMapping.class);
                if (getMapping != null && getMapping.value().length > 0) {
                    String endpoint = getMapping.value()[0];
                    xmlValidationService.validateXml(endpoint, "GET", "", xmlResponse);
                }
            }
        }

        return result;
    }

    @Around("@annotation(org.springframework.web.bind.annotation.PutMapping) && " +
            "execution(String *(..))")
    public Object validatePutStringResponse(ProceedingJoinPoint joinPoint) throws Throwable {

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        LOGGER.info("PUT aspect triggered for method: " + method.getName());

        Object result = joinPoint.proceed();

        if (result instanceof String) {
            String xmlResponse = (String) result;
            LOGGER.info("PUT response is String, length: " + xmlResponse.length());

            PutMapping putMapping = method.getAnnotation(PutMapping.class);
            if (putMapping != null && putMapping.value().length > 0) {
                String endpoint = putMapping.value()[0];
                Object[] args = joinPoint.getArgs();
                String requestXml = "";
                for (Object arg : args) {
                    if (arg instanceof String && ((String) arg).contains("<")) {
                        requestXml = (String) arg;
                        break;
                    }
                }
                LOGGER.info("Validating PUT request: " + endpoint + ", has request XML: " + !requestXml.isEmpty());
                xmlValidationService.validateXml(endpoint, "PUT", requestXml, xmlResponse);
            }
        } else {
            LOGGER.warning("PUT response is not String, type: " + (result != null ? result.getClass().getName() : "null"));
        }

        return result;
    }
}
