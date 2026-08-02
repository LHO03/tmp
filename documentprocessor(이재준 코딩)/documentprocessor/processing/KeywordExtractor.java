package documentprocessor.processing;

import documentprocessor.Document;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class KeywordExtractor {
    public List<String> extractKeywords(Document document) {
        // 문서 내용에서 키워드 추출 로직 구현
        // 여기서는 간단히 공백으로 분리된 단어들을 키워드로 반환
        return Arrays.stream(document.getContent().split("\\s+"))
                     .map(s -> s.replaceAll("[^a-zA-Z0-9가-힣]", "")) // 특수문자 제거
                     .filter(s -> !s.isEmpty() && s.length() > 2) // 짧은 단어 및 빈 문자열 제거
                     .distinct() // 중복 제거
                     .collect(Collectors.toList());
    }
}
