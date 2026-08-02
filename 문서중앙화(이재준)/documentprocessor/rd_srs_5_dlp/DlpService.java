package documentprocessor.rd_srs_5_dlp;

import documentprocessor.core.MatchResult;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 데이터 유출 방지(DLP) 기능을 총괄하는 서비스 클래스입니다. (RD-SRS-5)
 * 정형(Pattern) 및 비정형(Heuristic) 탐지 서비스를 통합하여 민감 정보를 스캔합니다.
 */
public class DlpService {

    private final PatternDetectionService patternDetector;
    private final HeuristicDetectionService heuristicDetector;

    public DlpService() {
        this.patternDetector = new PatternDetectionService();
        this.heuristicDetector = new HeuristicDetectionService();
    }

    public Map<String, Object> checkSensitiveData(byte[] document, String filename) {
        String documentText = new String(document);

        List<MatchResult> regexMatches = patternDetector.identifySensitivePatterns(documentText);
        double mlScore = heuristicDetector.mlDetectSensitiveData(documentText);

        Map<String, Object> result = new HashMap<>();
        result.put("has_sensitive", !regexMatches.isEmpty() || mlScore > 0.5);
        result.put("matches", regexMatches);
        result.put("ml_score", mlScore);

        return result;
    }

    public Map<String, Object> checkSensitiveDataOnUpdate(String documentDiff) {
        List<MatchResult> regexMatches = patternDetector.identifySensitivePatterns(documentDiff);
        double mlScore = heuristicDetector.mlDetectSensitiveData(documentDiff);

        Map<String, Object> result = new HashMap<>();
        result.put("has_sensitive", !regexMatches.isEmpty() || mlScore > 0.5);
        result.put("matches", regexMatches);
        result.put("ml_score", mlScore);

        return result;
    }
}
