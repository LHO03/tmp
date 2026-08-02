package documentprocessor.rd_srs_6_behavior_analysis;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 사용자 행위 분석(Behavior Analysis) 기능을 총괄하는 서비스 클래스입니다. (RD-SRS-6)
 */
public class BehaviorAnalysisService {

    private final RealTimeAnomalyService realTimeAnomalyService;
    private final PatternAnalysisService patternAnalysisService;
    private final ThreatPrioritizationService threatPrioritizationService;
    private final ModelManagementService modelManagementService;

    public BehaviorAnalysisService() {
        this.realTimeAnomalyService = new RealTimeAnomalyService();
        this.patternAnalysisService = new PatternAnalysisService();
        this.threatPrioritizationService = new ThreatPrioritizationService();
        this.modelManagementService = new ModelManagementService();
    }

    public Map<String, Object> detectBehaviorAnomaly(String userId, Map<String, Object> activityLog) {
        return realTimeAnomalyService.detectBehaviorAnomaly(userId, activityLog);
    }

    public Map<String, Object> analyzeAccessPatterns(String userId, LocalDateTime startTime, LocalDateTime endTime) {
        return patternAnalysisService.analyzeAccessPatterns(userId, startTime, endTime);
    }

    public List<Map<String, Object>> prioritizeThreats(List<Map<String, Object>> threats) {
        return threatPrioritizationService.prioritizeThreats(threats);
    }

    public boolean updateModel(List<Map<String, Object>> trainingData) {
        return modelManagementService.updateModel(trainingData);
    }

    public boolean feedbackFalsePositives(String dataId, String label) {
        return modelManagementService.feedbackFalsePositives(dataId, label);
    }
}
