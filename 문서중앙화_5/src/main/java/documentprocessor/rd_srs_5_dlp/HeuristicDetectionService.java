package documentprocessor.rd_srs_5_dlp;

import documentprocessor.rd_srs_5_dlp.config.HeuristicConfig;
import documentprocessor.rd_srs_5_dlp.config.HeuristicRuleRepository;
import documentprocessor.rd_srs_5_dlp.config.Keyword;

/**
 * '대외비', '기밀' 등 민감 키워드 출현 빈도를 기반으로 비정형 데이터를 탐지합니다. (RD-SRS-5.4)
 * 규칙은 외부 YAML에서 로드됩니다.
 */
public class HeuristicDetectionService {

    private final HeuristicConfig cfg;

    public HeuristicDetectionService(HeuristicRuleRepository repo) {
        this.cfg = repo.load();
    }

    /**
     * 비정형 데이터나 문맥에 따라 민감도가 달라지는 정보를 탐지하기 위한 시뮬레이션 기능입니다.
     * @param documentText 분석할 문서 텍스트
     * @return 민감도 점수 (0.0 ~ 1.0)
     */
    public double mlDetectSensitiveData(String documentText) {
        String lowerCaseText = documentText.toLowerCase();
        int sensitiveKeywordCount = 0;

        for (Keyword k : cfg.keywords()) {
            String token = k.token().toLowerCase();
            int lastIndex = 0;
            while ((lastIndex = lowerCaseText.indexOf(token, lastIndex)) != -1) {
                sensitiveKeywordCount += Math.max(1, k.weight());
                lastIndex += token.length();
            }
        }
        return Math.min(1.0, (double) sensitiveKeywordCount / cfg.normalize_divisor());
    }

    public double threshold() { return cfg.threshold(); }
}