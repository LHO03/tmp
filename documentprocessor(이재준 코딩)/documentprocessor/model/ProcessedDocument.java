package documentprocessor.model;

import java.util.List;

public class ProcessedDocument {
    private String extractedText;
    private String category;
    private List<String> keywords;

    public ProcessedDocument(String extractedText, String category, List<String> keywords) {
        this.extractedText = extractedText;
        this.category = category;
        this.keywords = keywords;
    }

    public String getExtractedText() {
        return extractedText;
    }

    public String getCategory() {
        return category;
    }

    public List<String> getKeywords() {
        return keywords;
    }
}
