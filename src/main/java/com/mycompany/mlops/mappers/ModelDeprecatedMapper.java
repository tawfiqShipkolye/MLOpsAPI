package com.mycompany.mlops.mappers;

import com.mycompany.mlops.exceptions.ModelDeprecatedException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.Map;

@Provider
public class ModelDeprecatedMapper implements ExceptionMapper<ModelDeprecatedException> {

    @Override
    public Response toResponse(ModelDeprecatedException e) {
        return Response.status(Response.Status.FORBIDDEN)
                .type(MediaType.APPLICATION_JSON)
                .entity(Map.of(
                        "status", 403,
                        "error", "Forbidden",
                        "message", e.getMessage()
                )).build();
    }
}