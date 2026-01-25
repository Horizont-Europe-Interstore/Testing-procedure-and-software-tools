# XML Validation Feature

This feature automatically validates XML responses from RESTful endpoints against expected XML files using XMLUnit.

## How it works

1. **Automatic Validation**: The `XmlValidationInterceptor` automatically captures XML responses from configured endpoints
2. **Expected XML Files**: Store expected XML responses in `src/main/resources/expected-xml/`
3. **File Naming Convention**: `{sanitized_endpoint}_{http_method}.xml`
   - Example: `/dcap` GET request → `_dcap_get.xml`
   - Example: `/edev/0` PUT request → `_edev_0_put.xml`

## Expected XML File Structure

```
src/main/resources/expected-xml/
├── _dcap_get.xml              # GET /dcap
├── _edev_get.xml              # GET /edev  
├── _edev_0_get.xml            # GET /edev/0
├── _der_get.xml               # GET /der
└── _fsa_get.xml               # GET /fsa
```

## Frontend Integration

The React frontend displays validation results with:
- ✅ **Request XML**: The original request body
- 📄 **Expected XML**: The expected response from file
- 🔍 **Actual XML**: The actual server response
- ❌ **Differences**: XMLUnit comparison results (if validation fails)

## API Endpoints

- `GET /api/xml-validation/results` - Get all validation results
- `GET /api/xml-validation/results/{id}` - Get specific validation result
- `POST /api/xml-validation/validate` - Manual validation
- `DELETE /api/xml-validation/results` - Clear all results

## Usage

1. **Add Expected XML Files**: Create XML files in the expected-xml folder
2. **Make API Calls**: Hit your RESTful endpoints (GET, POST, PUT, etc.)
3. **View Results**: Check the "XML Validation" tab in the React frontend
4. **Review Differences**: Expand failed validations to see XMLUnit differences

## Configuration

The interceptor is configured to validate these endpoints:
- `/dcap`
- `/der/**`
- `/edev/**` 
- `/fsa/**`
- `/sdev/**`
- `/tm/**`

To add more endpoints, update `XmlValidationWebConfig.java`.