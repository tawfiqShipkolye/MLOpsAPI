package com.mycompany.mlops.resources;

import com.mycompany.mlops.exceptions.LinkedWorkspaceNotFoundException;
import com.mycompany.mlops.model.MachineLearningModel;
import com.mycompany.mlops.model.MLWorkspace;
import com.mycompany.mlops.storage.DataStore;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Path("/models")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ModelResource {

    private final DataStore store = DataStore.getInstance();

    @GET
    public Response getModels(@QueryParam("status") String status) {
        List<MachineLearningModel> result = new ArrayList<>(store.getModels().values());
        if (status != null && !status.isBlank()) {
            result.removeIf(m -> !m.getStatus().equalsIgnoreCase(status));
        }
        return Response.ok(result).build();
    }

    @POST
    public Response createModel(MachineLearningModel model) {
        MLWorkspace workspace = store.getWorkspaces().get(model.getWorkspaceId());
        if (workspace == null) {
            throw new LinkedWorkspaceNotFoundException(
                    "Workspace with id '" + model.getWorkspaceId() + "' does not exist.");
        }
        store.getModels().put(model.getId(), model);
        store.getMetrics().put(model.getId(), new ArrayList<>());
        workspace.getModelIds().add(model.getId());
        return Response.status(Response.Status.CREATED).entity(model).build();
    }

    @Path("/{modelId}/metrics")
    public EvaluationMetricResource getMetricResource(@PathParam("modelId") String modelId) {
        return new EvaluationMetricResource(modelId);
    }
}