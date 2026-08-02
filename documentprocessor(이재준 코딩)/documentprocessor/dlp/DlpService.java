package documentprocessor.dlp;

import documentprocessor.MatchResult;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DlpService {

    // 정규식 패턴 정의 (예시)
    private static final Pattern SSN_PATTERN = Pattern.compile("\d{6}[ -]\d{7}"); // 주민등록번호
    private static final Pattern ACCOUNT_PATTERN = Pattern.compile("\b\d{3}[ -]\d{2}[ -]\d{5}\b"); // 계좌번호 (예시)

    /**
     * 업로드된 문서 내 민감정보 포함 여부를 자동으로 판별합니다.
     * 적용 요구사항: RD-SRS-5.1, 5.4, 6.1
     *
     * @param document 문서 내용 (바이트 배열)
     * @param filename 파일명
     * @return 민감정보 탐지 결과 맵
     */
    public Map<String, Object> checkSensitiveData(byte[] document, String filename) {
        String documentText = new String(document); // 바이트 배열을 문자열로 변환 (인코딩 고려 필요)

        List<MatchResult> matches = identifySensitivePatterns(documentText);
        double mlScore = mlDetectSensitiveData(documentText);

        Map<String, Object> result = new HashMap<>();
        result.put("has_sensitive", !matches.isEmpty() || mlScore > 0.5); // ML 스코어 임계값은 예시
        result.put("matches", matches);
        result.put("ml_score", mlScore);
        return result;
    }

    /**
     * 문서 수정 시 변경된 부분에 대해 실시간 민감정보 탐지 수행합니다.
     * 적용 요구사항: RD-SRS-5.2
     *
     * @param documentDiff 문서 변경 내용 (diff 문자열)
     * @return 민감정보 탐지 결과 맵
     */
    public Map<String, Object> checkSensitiveDataOnUpdate(String documentDiff) {
        List<MatchResult> diffMatches = identifySensitivePatterns(documentDiff);
        double mlScore = mlDetectSensitiveData(documentDiff);

        Map<String, Object> result = new HashMap<>();
        result.put("has_sensitive", !diffMatches.isEmpty() || mlScore > 0.5);
        result.put("diff_matches", diffMatches);
        result.put("ml_score", mlScore);
        return result;
    }

    /**
     * 다양한 형식의 민감 데이터를 정규식 기반으로 탐지합니다.
     * 적용 요구사항: RD-SRS-5.4
     *
     * @param documentText 문서 텍스트
     * @return 탐지된 민감 정보 목록
     */
    public List<MatchResult> identifySensitivePatterns(String documentText) {
        List<MatchResult> matches = new ArrayList<>();

        // 주민등록번호 탐지
        Matcher ssnMatcher = SSN_PATTERN.matcher(documentText);
        while (ssnMatcher.find()) {
            matches.add(new MatchResult("SSN", ssnMatcher.group(), ssnMatcher.start()));
        }

        // 계좌번호 탐지
        Matcher accountMatcher = ACCOUNT_PATTERN.matcher(documentText);
        while (accountMatcher.find()) {
            matches.add(new MatchResult("Account", accountMatcher.group(), accountMatcher.start()));
        }

        // TODO: 키워드/패턴 탐지 로직 추가 (예: 특정 키워드 목록과 비교)
        // TODO: 다른 민감 정보 정규식 추가 (예: 전화번호, 이메일 등)

        return matches;
    }

    /**
     * 머신러닝 분류 모델(BERT 등)을 통한 민감도 판별을 시뮬레이션합니다.
     * 적용 요구사항: RD-SRS-5.5, 6.1
     *
     * @param document 문서 텍스트
     * @return 민감도 점수 (0.0 ~ 1.0)
     */
    public double mlDetectSensitiveData(String document) {
        // 실제 머신러닝 모델 연동 로직이 필요합니다.
        // 여기서는 단순히 문서 길이에 따라 임의의 점수를 반환하는 예시입니다.
        // 실제 구현 시에는 외부 ML 라이브러리 (예: TensorFlow for Java, Deeplearning4j) 또는
        // ML 모델 서빙 API (예: Flask/FastAPI로 구현된 Python ML 서버)와 연동해야 합니다.
        if (document == null || document.isEmpty()) {
            return 0.0;
        }
        // 문서 길이에 비례하여 민감도 점수가 높아진다고 가정 (매우 단순화된 예시)
        return Math.min(1.0, document.length() / 1000.0);
    }
}
