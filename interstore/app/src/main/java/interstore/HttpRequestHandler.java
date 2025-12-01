package interstore;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.json.JSONObject;
import java.util.logging.Logger;

@Component
public class HttpRequestHandler {
    private static final Logger LOGGER = Logger.getLogger(HttpRequestHandler.class.getName());
    private final RestTemplate restTemplate = new RestTemplate();
    
    @Autowired
    private NatsPublisher natsPublisher;
    
    public String forwardToBackend(String method, String path, String body, String contentType, String lfdi, String sfdi, String correlationId) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", contentType != null ? contentType : "application/sep+xml");
            headers.set("X-Server-LFDI", lfdi);
            headers.set("X-Server-SFDI", sfdi);
            
            HttpEntity<String> entity = new HttpEntity<>(body, headers);
            
            String backendUrl = "http://localhost:8080" + path;
            
            ResponseEntity<String> response;
            switch (method.toUpperCase()) {
                case "GET":
                    response = restTemplate.exchange(backendUrl, HttpMethod.GET, entity, String.class);
                    break;
                case "POST":
                    response = restTemplate.exchange(backendUrl, HttpMethod.POST, entity, String.class);
                    break;
                case "PUT":
                    response = restTemplate.exchange(backendUrl, HttpMethod.PUT, entity, String.class);
                    break;
                case "DELETE":
                    response = restTemplate.exchange(backendUrl, HttpMethod.DELETE, entity, String.class);
                    break;
                default:
                    String errorResponse = "HTTP/1.1 405 Method Not Allowed\r\n\r\n";
                    natsPublisher.publish("ieee2030.responses." + correlationId, errorResponse);
                    return errorResponse;
            }
            
            String httpResponse = buildHttpResponse(response.getBody(), path, lfdi, sfdi);
            natsPublisher.publish("ieee2030.responses." + correlationId, httpResponse);
            return httpResponse;
            
        } catch (Exception e) {
            LOGGER.severe("Error forwarding request: " + e.getMessage());
            String errorResponse = "HTTP/1.1 500 Internal Server Error\r\n\r\n";
            natsPublisher.publish("ieee2030.responses." + correlationId, errorResponse);
            return errorResponse;
        }
    }
    
    private String buildHttpResponse(String responseBody, String path, String lfdi, String sfdi) {
        StringBuilder response = new StringBuilder();
        response.append("HTTP/1.1 200 OK\r\n");
        response.append("Content-Type: application/sep+xml\r\n");
        response.append("Content-Length: ").append(responseBody != null ? responseBody.length() : 0).append("\r\n");
        
        if (path.equals("/dcap") || path.equals("/dcap/") || path.equals("/")) {
            response.append("X-Server-LFDI: ").append(lfdi).append("\r\n");
            response.append("X-Server-SFDI: ").append(sfdi).append("\r\n");
        }
        
        response.append("\r\n");
        if (responseBody != null) {
            response.append(responseBody);
        }
        
        return response.toString();
    }
}