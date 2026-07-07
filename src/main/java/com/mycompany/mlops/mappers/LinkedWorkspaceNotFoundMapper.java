package com.mycompany.mlops.mappers;

import com.mycompany.mlops.exceptions.LinkedWorkspaceNotFoundException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.Map;

@Provider
public class LinkedWorkspaceNotFoundMapper implements ExceptionMapper<LinkedWorkspaceNotFoundException> {

    @Override
    public Response toResponse(LinkedWorkspaceNotFoundException e) {
        return Response.status(422)
                .type(MediaType.APPLICATION_JSON)
                .entity(Map.of(
                        "status", 422,
                        "error", "Unprocessable Entity",
                        "message", e.getMessage()
                )).build();
    }
}