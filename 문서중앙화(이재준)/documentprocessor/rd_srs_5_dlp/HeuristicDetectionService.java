package documentprocessor.rd_srs_5_dlp;

import java.util.List;

/**
 * '대외비', '기밀' 등 민감 키워드 출현 빈도를 기반으로 비정형 데이터를 탐지합니다. (RD-SRS-5.2)
 */
public class HeuristicDetectionService {

    private static final List<String> SENSITIVE_KEYWORDS = List.of("대외비", "기밀", "보안", "개인정보", "유출금지");

    /**
     * 비정형 데이터나 문맥에 따라 민감도가 달라지는 정보를 탐지하기 위한 시뮬레이션 기능입니다.
     *
     * @param documentText 분석할 문서 텍스트
     * @return 민감도 점수 (0.0 ~ 1.0)
     */
    public double mlDetectSensitiveData(String documentText) {
        String lowerCaseText = documentText.toLowerCase();
        int sensitiveKeywordCount = 0;

        for (String keyword : SENSITIVE_KEYWORDS) {
            int lastIndex = 0;
            while ((lastIndex = lowerCaseText.indexOf(keyword, lastIndex)) != -1) {
                sensitiveKeywordCount++;
                lastIndex += keyword.length();
            }
        }

        return Math.min(1.0, (double) sensitiveKeywordCount / 10.0);
    }
}
