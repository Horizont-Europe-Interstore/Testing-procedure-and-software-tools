# XML Validation Service - IEEE 2030.5 Conformance Testing

## Overview
This service validates XML payloads exchanged between the device (client) and testing software (server) against the IEEE 2030.5 WADL specification.

## How It Works

### 1. WADL-Based Validation
- **WADL File**: `sep_wadl.xml` contains the IEEE 2030.5 API specification
- **Expected Values**: Extracted from WADL at startup (endpoint + HTTP method → expected XML element)
- **Actual Values**: Real XML payloads exchanged between device and server

### 2. Validation Flow
```
Device Request → Server Response → Interceptor → Validation Service
                                                        ↓
                                    Compare actual XML root element 
                                    with WADL expected element
                                                        ↓
                                    Store result + Publish to NATS
```

### 3. Polling Endpoints (Excluded)
These endpoints are **NOT validated** as they're not stored in the server:
- `/dcap` - Device Capability
- `/tm` - Time

### 4. Validation Checks
1. **Well-formed XML**: Can the XML be parsed?
2. **Root Element Match**: Does the actual XML root element match WADL expectations?
   - Example: GET `/edev/1` should return `<EndDevice>` element

## Files

### Core Files (Keep)
- **XmlValidationService.java**: Main validation logic using WADL
- **XmlValidationResult.java**: Data model for validation results
- **XmlValidationController.java**: REST API for validation results
- **XmlValidationInterceptor.java**: Intercepts HTTP responses
- **ContentCachingFilter.java**: Caches request/response bodies
- **XmlValidationWebConfig.java**: Spring configuration
- **sep_wadl.xml**: IEEE 2030.5 WADL specification

## API Endpoints

### Validate XML
```http
POST /api/xml-validation/validate
Content-Type: application/json

{
  "endpoint": "/edev/1",
  "httpMethod": "GET",
  "requestXml": "",
  "actualXml": "<EndDevice>...</EndDevice>"
}
```

### Get All Results
```http
GET /api/xml-validation/results
```

### Get Specific Result
```http
GET /api/xml-validation/results/{id}
```

### Clear Results
```http
DELETE /api/xml-validation/results
```

## Example Validation

### Scenario: GET /edev/1
1. **WADL Says**: GET `/edev/{id1}` should return `<EndDevice>` element
2. **Server Returns**: `<EndDevice xmlns="urn:ieee:std:2030.5:ns">...</EndDevice>`
3. **Validation**: 
   - ✅ Well-formed XML
   - ✅ Root element = "EndDevice"
   - ✅ Matches WADL expectation
4. **Result**: Valid = true, Differences = "Valid - matches WADL"

### Scenario: GET /derp/1/dercap (Wrong Response)
1. **WADL Says**: GET `/derp/{id1}/dercap` should return `<DERCapability>`
2. **Server Returns**: `<DERSettings>...</DERSettings>` (wrong!)
3. **Validation**:
   - ✅ Well-formed XML
   - ❌ Root element = "DERSettings" (expected "DERCapability")
4. **Result**: Valid = false, Differences = "Expected: DERCapability, Actual: DERSettings"

## NATS Integration
All validation results are published to NATS topic: `ieee2030.test.results`

This allows other EC2 instances to receive validation results in real-time.

## Configuration

### Add More Polling Endpoints
Edit `XmlValidationService.java`:
```java
private final Set<String> pollingEndpoints = Set.of("/dcap", "/tm", "/your-endpoint");
```

### Add More Validation Patterns
Edit `XmlValidationWebConfig.java`:
```java
.addPathPatterns("/der/**", "/edev/**", "/your-pattern/**")
```
