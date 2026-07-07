package com.mycompany.mlops.resources;

import com.mycompany.mlops.exceptions.LinkedWorkspaceNotFoundException;
import com.mycompany.mlops.exceptions.WorkspaceNotEmptyException;
import com.mycompany.mlops.model.MLWorkspace;
import com.mycompany.mlops.storage.DataStore;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collection;
import java.util.Map;

@Path("/workspaces")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class WorkspaceResource {

    private final DataStore store = DataStore.getInstance();

    @GET
    public Response getAllWorkspaces() {
        Collection<MLWorkspace> workspaces = store.getWorkspaces().values();
        return Response.ok(workspaces).build();
    }

    @POST
    public Response createWorkspace(MLWorkspace workspace) {
        if (workspace.getId() == null || workspace.getId().isBlank()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", "Workspace id is required")).build();
        }
        store.getWorkspaces().put(workspace.getId(), workspace);
        return Response.status(Response.Status.CREATED).entity(workspace).build();
    }

    @GET
    @Path("/{workspaceId}")
    public Response getWorkspace(@PathParam("workspaceId") String workspaceId) {
        MLWorkspace workspace = store.getWorkspaces().get(workspaceId);
        if (workspace == null) {
            throw new LinkedWorkspaceNotFoundException("Workspace not found: " + workspaceId);
        }
        return Response.ok(workspace).build();
    }

    @DELETE
    @Path("/{workspaceId}")
    public Response deleteWorkspace(@PathParam("workspaceId") String workspaceId) {
        MLWorkspace workspace = store.getWorkspaces().get(workspaceId);
        if (workspace == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("error", "Workspace not found: " + workspaceId)).build();
        }
        if (!workspace.getModelIds().isEmpty()) {
            throw new WorkspaceNotEmptyException("Workspace " + workspaceId + " still has models assigned.");
        }
        store.getWorkspaces().remove(workspaceId);
        return Response.noContent().build();
    }
}