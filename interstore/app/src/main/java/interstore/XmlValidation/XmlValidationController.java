package interstore.XmlValidation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/xml-validation")
@CrossOrigin(origins = "*")
public class XmlValidationController {

    @Autowired
    private XmlValidationService xmlValidationService;

    @PostMapping("/validate")
    public ResponseEntity<XmlValidationResult> validateXml(@RequestBody XmlValidationRequest request) {
        XmlValidationResult result = xmlValidationService.validateXml(
            request.getEndpoint(),
            request.getHttpMethod(),
            request.getRequestXml(),
            request.getActualXml()
        );
        return ResponseEntity.ok(result);
    }

    @GetMapping("/results")
    public ResponseEntity<List<XmlValidationResult>> getAllResults() {
        return ResponseEntity.ok(xmlValidationService.getAllValidationResults());
    }

    @GetMapping("/results/{id}")
    public ResponseEntity<XmlValidationResult> getResult(@PathVariable String id) {
        XmlValidationResult result = xmlValidationService.getValidationResult(id);
        if (result != null) {
            return ResponseEntity.ok(result);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/results")
    public ResponseEntity<Void> clearResults() {
        xmlValidationService.clearResults();
        return ResponseEntity.ok().build();
    }

    public static class XmlValidationRequest {
        private String endpoint;
        private String httpMethod;
        private String requestXml;
        private String actualXml;

      
        public String getEndpoint() { return endpoint; }
        public void setEndpoint(String endpoint) { this.endpoint = endpoint; }

        public String getHttpMethod() { return httpMethod; }
        public void setHttpMethod(String httpMethod) { this.httpMethod = httpMethod; }

        public String getRequestXml() { return requestXml; }
        public void setRequestXml(String requestXml) { this.requestXml = requestXml; }

        public String getActualXml() { return actualXml; }
        public void setActualXml(String actualXml) { this.actualXml = actualXml; }
    }
}