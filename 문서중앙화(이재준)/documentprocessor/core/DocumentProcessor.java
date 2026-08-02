package documentprocessor.core;

import documentprocessor.core.model.ProcessedDocument;
import documentprocessor.core.processing.DocumentCategorizer;
import documentprocessor.core.processing.KeywordExtractor;
import documentprocessor.core.processing.TextExtractor;

import java.util.List;

/**
 * 문서 처리의 전체 흐름을 관장하는 오케스트레이터(Orchestrator) 클래스입니다.
 * 텍스트 추출, 분류, 키워드 추출의 파이프라인을 순차적으로 실행합니다.
 */
public class DocumentProcessor {

    private final TextExtractor textExtractor;
    private final DocumentCategorizer documentCategorizer;
    private final KeywordExtractor keywordExtractor;

    /**
     * DocumentProcessor의 새 인스턴스를 생성합니다.
     * 필요한 의존성(TextExtractor, DocumentCategorizer, KeywordExtractor)을 주입받습니다.
     */
    public DocumentProcessor() {
        this.textExtractor = new TextExtractor();
        this.documentCategorizer = new DocumentCategorizer();
        this.keywordExtractor = new KeywordExtractor();
    }

    /**
     * 입력된 Document 객체를 받아 텍스트 추출, 분류, 키워드 추출의 파이프라인을
     * 순차적으로 실행하고, 최종 결과를 ProcessedDocument 객체로 반환합니다.
     *
     * @param document 처리할 원본 Document 객체
     * @return 처리된 문서의 결과(텍스트, 카테고리, 키워드)를 담은 ProcessedDocument 객체
     */
    public ProcessedDocument processDocument(Document document) {
        // 1. 텍스트 추출
        String extractedText = textExtractor.extractText(document);
        System.out.println("Extracted Text: " + (extractedText.length() > 100 ? extractedText.substring(0, 100) + "..." : extractedText));

        // 2. 문서 분류
        String category = documentCategorizer.categorize(extractedText);
        System.out.println("Categorized as: " + category);

        // 3. 키워드 추출
        List<String> keywords = keywordExtractor.extractKeywords(extractedText);
        System.out.println("Extracted Keywords: " + keywords);

        // 처리된 문서 객체 반환
        return new ProcessedDocument(extractedText, category, keywords);
    }
}