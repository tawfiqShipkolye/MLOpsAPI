package com.mycompany.mlops.storage;

import com.mycompany.mlops.model.EvaluationMetric;
import com.mycompany.mlops.model.MachineLearningModel;
import com.mycompany.mlops.model.MLWorkspace;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DataStore {

    private static final DataStore INSTANCE = new DataStore();

    private final Map<String, MLWorkspace> workspaces = new ConcurrentHashMap<>();
    private final Map<String, MachineLearningModel> models = new ConcurrentHashMap<>();
    private final Map<String, List<EvaluationMetric>> metrics = new ConcurrentHashMap<>();

    private DataStore() {
        MLWorkspace w1 = new MLWorkspace("WS-VISION-01", "Computer Vision Lab", 500);
        MLWorkspace w2 = new MLWorkspace("WS-NLP-01", "NLP Research Team", 300);
        workspaces.put(w1.getId(), w1);
        workspaces.put(w2.getId(), w2);

        MachineLearningModel m1 = new MachineLearningModel("MOD-8832", "TensorFlow", "DEPLOYED", 0.95, "WS-VISION-01");
        models.put(m1.getId(), m1);
        w1.getModelIds().add(m1.getId());
        metrics.put(m1.getId(), new ArrayList<>());
    }

    public static DataStore getInstance() { return INSTANCE; }

    public Map<String, MLWorkspace> getWorkspaces() { return workspaces; }
    public Map<String, MachineLearningModel> getModels() { return models; }
    public Map<String, List<EvaluationMetric>> getMetrics() { return metrics; }
}