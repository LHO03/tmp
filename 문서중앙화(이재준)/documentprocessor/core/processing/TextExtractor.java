package documentprocessor.core.processing;

import documentprocessor.core.Document;
import documentprocessor.core.DocumentType;

/**
 * PDF, DOCX, TXT 등 다양한 포맷의 문서에서 순수 텍스트 콘텐츠를 추출합니다.
 * 현재는 간단한 텍스트 추출 로직을 시뮬레이션합니다.
 */
public class TextExtractor {

    /**
     * 주어진 문서 객체에서 텍스트를 추출합니다.
     * 실제 구현에서는 파일 포맷에 따라 다른 라이브러리(예: Apache Tika)를 사용할 수 있습니다.
     *
     * @param document 텍스트를 추출할 Document 객체
     * @return 추출된 순수 텍스트
     */
    public String extractText(Document document) {
        // 실제 구현에서는 문서 타입에 따라 다른 추출 로직을 사용합니다.
        // 여기서는 간단히 바이트 배열을 문자열로 변환하는 것을 시뮬레이션합니다.
        if (document.getContent() == null || document.getContent().length == 0) {
            return "";
        }

        // 예시: 문서 타입에 따른 가상 텍스트 추출
        String extracted = new String(document.getContent());
        switch (document.getType()) {
            case INVOICE:
                return "[INVOICE TEXT] " + extracted;
            case CONTRACT:
                return "[CONTRACT TEXT] " + extracted;
            case LEGAL:
                return "[LEGAL TEXT] " + extracted;
            case FINANCIAL:
                return "[FINANCIAL TEXT] " + extracted;
            case GENERAL:
                return "[GENERAL TEXT] " + extracted;
            case CONFIDENTIAL:
                return "[CONFIDENTIAL TEXT] " + extracted;
            default:
                return "[UNKNOWN TYPE TEXT] " + extracted;
        }
    }
}