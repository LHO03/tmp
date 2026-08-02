package documentprocessor.core;

/**
 * 문서의 종류를 정의하는 열거형(Enum)입니다.
 * 시스템이 처리할 수 있는 다양한 문서 유형을 나타냅니다.
 */
public enum DocumentType {
    INVOICE,      // 송장
    CONTRACT,     // 계약서
    LEGAL,        // 법률 문서
    FINANCIAL,    // 재무 문서
    GENERAL,      // 일반 문서
    CONFIDENTIAL, // 대외비 문서
    UNKNOWN       // 알 수 없는 유형
}