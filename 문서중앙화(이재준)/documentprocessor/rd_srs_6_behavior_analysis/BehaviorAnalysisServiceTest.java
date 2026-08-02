package documentprocessor.rd_srs_6_behavior_analysis;

import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

class BehaviorAnalysisServiceTest {

    private final BehaviorAnalysisService service = new BehaviorAnalysisService();

    @Test
    void detectBehaviorAnomaly_shouldDetectUnusualAccess() {
        // Given
        Map<String, Object> activityLog = new HashMap<>();
        activityLog.put("type", "unusual_access");

        // When
        Map<String, Object> result = service.detectBehaviorAnomaly("user123", activityLog);

        // Then
        assertTrue((Boolean) result.get("anomaly"));
        assertEquals("unusual_access", result.get("type"));
    }

    @Test
    void detectBehaviorAnomaly_shouldReturnNormal() {
        // Given
        Map<String, Object> activityLog = new HashMap<>();
        activityLog.put("type", "download");

        // When
        Map<String, Object> result = service.detectBehaviorAnomaly("user123", activityLog);

        // Then
        assertFalse((Boolean) result.get("anomaly"));
        assertEquals("normal", result.get("type"));
    }

    @Test
    void prioritizeThreats_shouldSortBySeverity() {
        // Given
        List<Map<String, Object>> threats = new ArrayList<>();
        threats.add(Map.of("severity", "Low"));
        threats.add(Map.of("severity", "High"));
        threats.add(Map.of("severity", "Medium"));

        // When
        List<Map<String, Object>> prioritizedThreats = service.prioritizeThreats(threats);

        // Then
        assertEquals("High", prioritizedThreats.get(0).get("severity"));
        assertEquals("Medium", prioritizedThreats.get(1).get("severity"));
        assertEquals("Low", prioritizedThreats.get(2).get("severity"));
    }
}
