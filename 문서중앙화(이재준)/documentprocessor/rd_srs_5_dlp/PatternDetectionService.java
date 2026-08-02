package documentprocessor.rd_srs_5_dlp;

import documentprocessor.core.MatchResult;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 정규식을 사용하여 주민등록번호, 계좌번호 등 정형화된 민감 정보 패턴을 탐지합니다. (RD-SRS-5.1)
 */
public class PatternDetectionService {

    private static final Pattern SSN_PATTERN = Pattern.compile("\\b\\d{6}[- ]?\\d{7}\\b"); // 주민등록번호
    private static final Pattern ACCOUNT_PATTERN = Pattern.compile("\\b\\d{3}[- ]?\\d{2}[- ]?\\d{5,6}\\b"); // 계좌번호 (예시)
    private static final Pattern PHONE_PATTERN = Pattern.compile("\\b010[- ]?\\d{4}[- ]?\\d{4}\\b"); // 휴대폰 번호
    private static final Pattern EMAIL_PATTERN = Pattern.compile("\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}\\b"); // 이메일 주소
    private static final Pattern CREDIT_CARD_PATTERN = Pattern.compile("\\b(?:4\\d{3}|5[1-5]\\d{2}|6011|3[47]\\d{2})[- ]?(?:\\d{4}[- ]?){3}\\d{3,4}\\b"); // 신용카드 번호 (예시)

    /**
     * 정규식을 사용하여 구조가 명확한 정형 데이터를 신속하고 정확하게 탐지합니다.
     *
     * @param documentText 탐지할 문서 텍스트
     * @return 탐지된 MatchResult 목록
     */
    public List<MatchResult> identifySensitivePatterns(String documentText) {
        List<MatchResult> matches = new ArrayList<>();

        findMatches(SSN_PATTERN, documentText, "SSN_PATTERN", matches);
        findMatches(ACCOUNT_PATTERN, documentText, "ACCOUNT_PATTERN", matches);
        findMatches(PHONE_PATTERN, documentText, "PHONE_PATTERN", matches);
        findMatches(EMAIL_PATTERN, documentText, "EMAIL_PATTERN", matches);
        findMatches(CREDIT_CARD_PATTERN, documentText, "CREDIT_CARD_PATTERN", matches);

        return matches;
    }

    private void findMatches(Pattern pattern, String text, String patternName, List<MatchResult> matches) {
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            matches.add(new MatchResult(patternName, matcher.group()));
        }
    }
}
