package documentprocessor.processing;

import documentprocessor.Document;
import documentprocessor.DocumentType;

public class DocumentCategorizer {
    public String categorize(Document document) {
        // 문서 내용을 기반으로 카테고리 분류 로직 구현
        // 여기서는 DocumentType을 기반으로 간단히 분류
        switch (document.getType()) {
            case INVOICE:
                return "Finance";
            case CONTRACT:
                return "Legal";
            case REPORT:
                return "General";
            default:
                return "Uncategorized";
        }
    }
}
