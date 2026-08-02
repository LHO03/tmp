package documentprocessor.core;

import java.util.UUID;

/**
 * 시스템에서 처리할 원본 문서를 나타내는 클래스입니다.
 * 각 문서는 고유 ID, 내용, 타입을 가집니다.
 */
public class Document {

    private final String id; // 문서의 고유 식별자
    private final byte[] content; // 문서의 바이너리 내용
    private final DocumentType type; // 문서의 종류 (예: INVOICE, CONTRACT)

    /**
     * Document 객체를 생성합니다.
     *
     * @param content 문서의 내용 (바이트 배열)
     * @param type    문서의 종류
     */
    public Document(byte[] content, DocumentType type) {
        this.id = UUID.randomUUID().toString(); // 새 문서에 고유 ID 할당
        this.content = content;
        this.type = type;
    }

    // Getter 메서드들
    public String getId() {
        return id;
    }

    public byte[] getContent() {
        return content;
    }

    public DocumentType getType() {
        return type;
    }
}