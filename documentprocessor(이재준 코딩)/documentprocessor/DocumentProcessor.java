package documentprocessor;

import documentprocessor.model.ProcessedDocument;
import documentprocessor.processing.DocumentCategorizer;
import documentprocessor.processing.KeywordExtractor;
import documentprocessor.processing.TextExtractor;

public class DocumentProcessor {
    private TextExtractor textExtractor;
    private DocumentCategorizer documentCategorizer;
    private KeywordExtractor keywordExtractor;

    public DocumentProcessor() {
        this.textExtractor = new TextExtractor();
        this.documentCategorizer = new DocumentCategorizer();
        this.keywordExtractor = new KeywordExtractor();
    }

    public ProcessedDocument processDocument(Document document) {
        String extractedText = textExtractor.extractText(document);
        String category = documentCategorizer.categorize(document);
        java.util.List<String> keywords = keywordExtractor.extractKeywords(document);

        return new ProcessedDocument(extractedText, category, keywords);
    }
}
