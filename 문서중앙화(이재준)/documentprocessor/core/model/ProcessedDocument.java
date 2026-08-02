package documentprocessor.core.model;

import java.util.List;

/**
 * 문서 처리 파이프라인의 최종 결과물을 담는 데이터 모델 클래스입니다.
 * 원본 문서에서 추출된 텍스트, 분류된 카테고리, 핵심 키워드 목록을 포함합니다.
 */
public class ProcessedDocument {

    private final String extractedText; // 정제된 순수 텍스트
    private final String category;      // 할당된 카테고리 (예: "Finance", "Legal")
    private final List<String> keywords;    // 추출된 키워드 목록

    /**
     * ProcessedDocument 객체를 생성합니다.
     *
     * @param extractedText 추출된 텍스트
     * @param category      분류된 카테고리
     * @param keywords      추출된 키워드 리스트
     */
    public ProcessedDocument(String extractedText, String category, List<String> keywords) {
        this.extractedText = extractedText;
        this.category = category;
        this.keywords = keywords;
    }

    // Getter 메서드들
    public String getExtractedText() {
        return extractedText;
    }

    public String getCategory() {
        return category;
    }

    public List<String> getKeywords() {
        return keywords;
    }

    @Override
    public String toString() {
        return "ProcessedDocument{" +
                "extractedText='" + (extractedText.length() > 50 ? extractedText.substring(0, 50) + "..." : extractedText) + "'" +
                ", category='" + category + "'" +
                ", keywords=" + keywords +
                '}';
    }
}