package documentprocessor;

public class Document {
    private String content;
    private DocumentType type;

    public Document(String content, DocumentType type) {
        this.content = content;
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public DocumentType getType() {
        return type;
    }
}
