package com.mycompany.mlops.resources;

import com.mycompany.mlops.exceptions.LinkedWorkspaceNotFoundException;
import com.mycompany.mlops.exceptions.ModelDeprecatedException;
import com.mycompany.mlops.model.EvaluationMetric;
import com.mycompany.mlops.model.MachineLearningModel;
import com.mycompany.mlops.storage.DataStore;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.UUID;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EvaluationMetricResource {

    private final String modelId;
    private final DataStore store = DataStore.getInstance();

    public EvaluationMetricResource(String modelId) {
        this.modelId = modelId;
    }

    @GET
    public Response getMetrics() {
        MachineLearningModel model = store.getModels().get(modelId);
        if (model == null) {
            throw new LinkedWorkspaceNotFoundException("Model not found: " + modelId);
        }
        List<EvaluationMetric> list = store.getMetrics().getOrDefault(modelId, List.of());
        return Response.ok(list).build();
    }

    @POST
    public Response addMetric(EvaluationMetric metric) {
        MachineLearningModel model = store.getModels().get(modelId);
        if (model == null) {
            throw new LinkedWorkspaceNotFoundException("Model not found: " + modelId);
        }
        if ("DEPRECATED".equalsIgnoreCase(model.getStatus())) {
            throw new ModelDeprecatedException(
                    "Model " + modelId + " is DEPRECATED and cannot accept new metrics.");
        }
        if (metric.getId() == null || metric.getId().isBlank()) {
            metric.setId(UUID.randomUUID().toString());
        }
        if (metric.getTimestamp() == 0) {
            metric.setTimestamp(System.currentTimeMillis());
        }
        store.getMetrics().get(modelId).add(metric);
        model.setLatestAccuracy(metric.getAccuracyScore());
        return Response.status(Response.Status.CREATED).entity(metric).build();
    }
}