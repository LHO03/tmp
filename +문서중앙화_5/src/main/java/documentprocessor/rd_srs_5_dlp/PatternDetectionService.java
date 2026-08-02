package documentprocessor.rd_srs_5_dlp;

import documentprocessor.core.MatchResult;
import documentprocessor.rd_srs_5_dlp.config.PatternRule;
import documentprocessor.rd_srs_5_dlp.config.PatternRuleRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 정규식을 사용하여 주민등록번호, 계좌번호 등 정형화된 민감 정보 패턴을 탐지합니다. (RD-SRS-5.3)
 * 규칙은 외부 YAML에서 로드됩니다.
 */
public class PatternDetectionService {

    private final List<Map.Entry<String, Pattern>> compiledRules; // name -> Pattern

    public PatternDetectionService(PatternRuleRepository repo) {
        this.compiledRules = repo.load().stream()
                .map(r -> Map.entry(r.name(), Pattern.compile(r.regex())))
                .collect(Collectors.toList());
    }

    /**
     * 정규식을 사용하여 구조가 명확한 정형 데이터를 신속하고 정확하게 탐지합니다.
     * @param documentText 탐지할 문서 텍스트
     * @return 탐지된 MatchResult 목록
     */
    public List<MatchResult> identifySensitivePatterns(String documentText) {
        List<MatchResult> matches = new ArrayList<>();
        for (Map.Entry<String, Pattern> e : compiledRules) {
            findMatches(e.getValue(), documentText, e.getKey(), matches);
        }
        return matches;
    }

    private void findMatches(Pattern pattern, String text, String patternName, List<MatchResult> matches) {
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            matches.add(new MatchResult(patternName, matcher.group()));
        }
    }
}