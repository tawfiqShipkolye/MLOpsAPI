# MLOps Pipeline Management API

A RESTful API built with JAX-RS (Jersey) and Grizzly for managing ML workspaces and models.

---

## How to Run

1. Clone the repo
2. Open in NetBeans
3. Right-click `Main.java` → Run File
4. Server starts at: `http://localhost:8080/api/v1/`

---

## curl Commands

# Discovery
curl -X GET http://localhost:8080/api/v1/

# Get all workspaces
curl -X GET http://localhost:8080/api/v1/workspaces

# Create a workspace
curl -X POST http://localhost:8080/api/v1/workspaces -H "Content-Type: application/json" -d '{"id":"WS-CV-02","teamName":"Deep Learning Lab","storageQuotaGb":200}'

# Get all models
curl -X GET http://localhost:8080/api/v1/models

# Filter models by status
curl -X GET "http://localhost:8080/api/v1/models?status=DEPLOYED"

# Create a model
curl -X POST http://localhost:8080/api/v1/models -H "Content-Type: application/json" -d '{"id":"MOD-001","framework":"PyTorch","status":"TRAINING","latestAccuracy":0.0,"workspaceId":"WS-NLP-01"}'

# Post a metric
curl -X POST http://localhost:8080/api/v1/models/MOD-8832/metrics -H "Content-Type: application/json" -d '{"accuracyScore":0.97}'

---

## Questions

### Part 1.1 — MessageBodyWriter and JSON Provider
When a Java object is returned from a JAX-RS resource method, it cannot be sent over HTTP as-is. A MessageBodyWriter is responsible for converting the Java object into a format that can be transmitted, in this case JSON. Jersey uses Jackson as the JSON provider, which automatically serialises Java objects into JSON by inspecting their fields and getters. Without a JSON provider registered, Jersey would not know how to convert the object and would throw an error. The jersey-media-json-jackson dependency handles this automatically once added to the project.

### Part 1.2 — Statelessness
Statelessness means that every HTTP request must contain all the information the server needs to process it. The server does not store any session or client state between requests. Each request is completely independent. This makes cloud APIs easier to scale horizontally because any server instance can handle any request — there is no need to route a client to the same server every time. New server instances can be added without sharing session data, making scaling simple and reliable.

### Part 2.1 — Cache-Control Headers
Adding Cache-Control headers to the GET /workspaces endpoint would allow clients and intermediary proxies to cache the response for a set period of time. This means repeated requests for the same data would be served from the cache rather than hitting the server, reducing processing load and improving response times. For example, setting Cache-Control: max-age=60 would tell the client to reuse the cached response for 60 seconds before making a new request.

### Part 2.2 — HEAD Method
The client should use the HEAD method. HEAD works exactly like GET but the server only returns the response headers without the body. This allows the client to check whether a workspace exists by looking at the status code (200 if it exists, 404 if not) without downloading the full JSON body, saving bandwidth.

### Part 3.1 — Server-Generated IDs
Allowing clients to supply their own IDs is a security and integrity risk. A client could supply a duplicate ID to overwrite an existing model, or supply a malformed ID that breaks the system. By generating IDs server-side using UUID.randomUUID(), the server guarantees uniqueness, prevents conflicts, and removes the risk of clients manipulating resource identifiers. It also ensures IDs follow a consistent format across all resources.

### Part 3.2 — URL Encoding
If a query parameter contains spaces or special characters, the client must URL-encode the value before sending. For example, "Scikit Learn & Tools" must be encoded as "Scikit+Learn+%26+Tools". This is necessary because URLs can only contain a limited set of characters. Spaces and symbols like & have special meaning in URLs and would break the request if sent unencoded. URL encoding converts these characters into a safe format that can be transmitted correctly.

### Part 4.1 — Class-Level vs Method-Level Annotations
Placing @Produces(MediaType.APPLICATION_JSON) at the class level means it applies to all methods in the class by default, avoiding repetition. If a specific method needs to return a different media type, it can override the class-level annotation by declaring its own @Produces annotation at the method level. This keeps the code clean and consistent while still allowing flexibility where needed.

### Part 5.2 — 4xx vs 5xx
A 4xx status code means the error was caused by the client — the request was invalid or contained incorrect data. A 5xx status code means the error was caused by the server — something went wrong internally that the client could not have prevented. When a client provides a non-existent workspaceId, it is the client's fault for sending bad data. The server processed the request correctly and determined it was invalid, so a 4xx code is the right choice.

### Part 5.4 — JAX-RS Exception Mapper Priority
JAX-RS always uses the most specific mapper available. If a LinkedWorkspaceNotFoundException is thrown and there is a specific ExceptionMapper for it, JAX-RS will use that mapper. The global ExceptionMapper<Throwable> is only used as a fallback when no specific mapper matches the exception type. This allows fine-grained control over error responses for known exceptions while still having a safety net for unexpected errors.

### Part 5.5 — ContainerRequestContext and ContainerResponseContext
Two pieces of crucial HTTP metadata that can be extracted are: first, the request URI from ContainerRequestContext using getUriInfo().getRequestUri(), which tells you exactly which endpoint was called and is essential for tracing issues; second, the HTTP status code from ContainerResponseContext using getStatus(), which tells you whether the request succeeded or failed and helps identify patterns of errors across the API.
