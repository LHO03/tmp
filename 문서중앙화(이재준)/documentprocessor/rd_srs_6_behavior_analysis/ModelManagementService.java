package documentprocessor.rd_srs_6_behavior_analysis;

import java.util.List;
import java.util.Map;

/**
 * 이상 행위 탐지 모델을 재학습하고 피드백을 반영합니다. (RD-SRS-6.4)
 */
public class ModelManagementService {

    public boolean updateModel(List<Map<String, Object>> trainingData) {
        System.out.println("Simulating model update with " + trainingData.size() + " data points.");
        return true;
    }

    public boolean feedbackFalsePositives(String dataId, String label) {
        System.out.println("Received feedback for dataId: " + dataId + ", label: " + label);
        return true;
    }
}
