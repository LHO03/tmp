package documentprocessor.core.processing;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 문서의 핵심 내용을 대표하는 주요 단어 또는 구(Phrase)를 추출합니다.
 * 현재는 간단한 공백 기반 단어 분리 및 필터링 로직을 사용합니다.
 */
public class KeywordExtractor {

    // 키워드로 간주하지 않을 불용어(Stopwords) 목록
    private static final List<String> STOPWORDS = Arrays.asList("a", "an", "the", "is", "in", "on", "of", "for", "to");

    /**
     * 텍스트에서 핵심 키워드를 추출합니다.
     *
     * @param text 키워드를 추출할 원본 텍스트
     * @return 추출된 키워드 목록
     */
    public List<String> extractKeywords(String text) {
        // 텍스트를 소문자로 변환하고, 구두점을 제거한 후 공백으로 단어 분리
        String[] words = text.toLowerCase().replaceAll("[^a-zA-Z ]", "").split("\\s+");

        // 불용어를 제외하고, 길이가 2 이상인 단어만 필터링하여 리스트로 반환
        return Arrays.stream(words)
                .filter(word -> !word.isEmpty() && word.length() > 1 && !STOPWORDS.contains(word))
                .distinct() // 중복 키워드 제거
                .collect(Collectors.toList());
    }
}