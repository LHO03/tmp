package documentprocessor.processing;

import documentprocessor.Document;

public class TextExtractor {
    public String extractText(Document document) {
        // 실제 텍스트 추출 및 정제 로직 구현
        // 여기서는 간단히 문서 내용을 반환
        return document.getContent();
    }
}
