package documentprocessor.rd_srs_6_behavior_analysis;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * 탐지된 여러 위협의 우선순위를 동적으로 결정합니다. (RD-SRS-6.3)
 */
public class ThreatPrioritizationService {

    public List<Map<String, Object>> prioritizeThreats(List<Map<String, Object>> threats) {
        threats.sort(Comparator.comparing(threat -> {
            String severity = (String) threat.getOrDefault("severity", "Low");
            return switch (severity) {
                case "High" -> 3;
                case "Medium" -> 2;
                case "Low" -> 1;
                default -> 0;
            };
        }, Comparator.reverseOrder()));
        return threats;
    }
}
