package documentprocessor.rd_srs_6_behavior_analysis;

import java.util.HashMap;
import java.util.Map;

/**
 * 실시간 사용자 활동 로그를 분석하여 이상 행위를 탐지합니다. (RD-SRS-6.1)
 */
public class RealTimeAnomalyService {

    public Map<String, Object> detectBehaviorAnomaly(String userId, Map<String, Object> activityLog) {
        Map<String, Object> result = new HashMap<>();
        result.put("anomaly", false);
        result.put("type", "normal");
        result.put("confidence", 0.0);

        if (activityLog.containsKey("type") && "unusual_access".equals(activityLog.get("type"))) {
            result.put("anomaly", true);
            result.put("type", "unusual_access");
            result.put("confidence", 0.8);
        }

        return result;
    }
}
