package documentprocessor.core;

import documentprocessor.core.model.ProcessedDocument;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DocumentProcessorTest {

    @Test
    void processDocument() {
        // Given
        DocumentProcessor processor = new DocumentProcessor();
        byte[] content = "This is a general document for testing purposes.".getBytes();
        Document document = new Document(content, DocumentType.GENERAL);

        // When
        ProcessedDocument processedDocument = processor.processDocument(document);

        // Then
        assertNotNull(processedDocument);
                assertEquals("[GENERAL TEXT] This is a general document for testing purposes.", processedDocument.getExtractedText());
                assertEquals("General", processedDocument.getCategory());
        assertFalse(processedDocument.getKeywords().isEmpty());
    }
}
