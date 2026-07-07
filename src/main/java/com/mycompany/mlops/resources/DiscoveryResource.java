package com.mycompany.mlops.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class DiscoveryResource {

    @GET
    public Response discover() {
        Map<String, Object> response = new HashMap<>();
        response.put("version", "1.0.0");
        response.put("name", "MLOps Pipeline Management API");
        response.put("admin_contact", "admin@mlops-lab.ac.uk");

        Map<String, String> links = new HashMap<>();
        links.put("workspaces", "/api/v1/workspaces");
        links.put("models", "/api/v1/models");
        response.put("resources", links);

        return Response.ok(response).build();
    }
}