package com.mycompany.mlops.mappers;

import com.mycompany.mlops.exceptions.WorkspaceNotEmptyException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.Map;

@Provider
public class WorkspaceNotEmptyMapper implements ExceptionMapper<WorkspaceNotEmptyException> {

    @Override
    public Response toResponse(WorkspaceNotEmptyException e) {
        return Response.status(Response.Status.CONFLICT)
                .type(MediaType.APPLICATION_JSON)
                .entity(Map.of(
                        "status", 409,
                        "error", "Conflict",
                        "message", e.getMessage()
                )).build();
    }
}