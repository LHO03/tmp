package documentprocessor.core.processing;

import java.util.Arrays;
import java.util.List;

/**
 * 추출된 텍스트의 내용을 분석하여 문서를 사전에 정의된 카테고리로 분류합니다.
 * 현재는 키워드 기반의 단순한 분류 로직을 사용합니다.
 */
public class DocumentCategorizer {

    // 카테고리별 키워드 정의
    private static final List<String> FINANCE_KEYWORDS = Arrays.asList("invoice", "payment", "receipt", "tax", "budget");
    private static final List<String> LEGAL_KEYWORDS = Arrays.asList("contract", "agreement", "law", "court", "legal");

    /**
     * 텍스트 내용을 분석하여 문서의 카테고리를 결정합니다.
     *
     * @param text 분석할 텍스트
     * @return 분류된 카테고리 문자열 (예: "Finance", "Legal", "General")
     */
    public String categorize(String text) {
        String lowerCaseText = text.toLowerCase();

        // 금융 관련 키워드가 포함되어 있는지 확인
        for (String keyword : FINANCE_KEYWORDS) {
            if (lowerCaseText.contains(keyword)) {
                return "Finance";
            }
        }

        // 법률 관련 키워드가 포함되어 있는지 확인
        for (String keyword : LEGAL_KEYWORDS) {
            if (lowerCaseText.contains(keyword)) {
                return "Legal";
            }
        }

        // 특정 카테고리에 속하지 않으면 일반으로 분류
        return "General";
    }
}