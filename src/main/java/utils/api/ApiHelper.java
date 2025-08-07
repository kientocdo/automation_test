package utils.api;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import java.util.Map;

public class ApiHelper {
    /**
     * @param method the HTTP method (GET, POST, PUT, DELETE)
     * @param url the endpoint URL
     * @param headers the request headers
     * @param body the request body
     * @param params the request parameters
     * @return the Response object
     */
    public Response makeRequest(RequestSpecification request, HttpMethod method, String url, Map<String, String> headers, String body, Map<String, String> params) {
        if (headers != null) {
            headers.forEach(request::header);
        }
        if (params != null) {
            params.forEach(request::queryParam);
        }
        if (body != null) {
            request.body(body);
        }

        // Make the request and get the response
        Response response = null;
        switch (method) {
            case GET:
                response = request.get(url);
                break;
            case POST:
                response = request.post(url);
                break;
            case PUT:
                response = request.put(url);
                break;
            case DELETE:
                response = request.delete(url);
                break;
            default:
                throw new IllegalArgumentException("Invalid HTTP method: " + method);
        }
        return response;
    }
}
