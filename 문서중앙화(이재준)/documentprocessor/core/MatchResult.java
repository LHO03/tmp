package documentprocessor.core;

/**
 * DLP (Data Loss Prevention) 기능에서 민감 정보 탐지 결과를 나타내는 레코드(Record) 클래스입니다.
 * 불변(immutable) 객체로, 탐지된 패턴과 해당 텍스트를 저장합니다.
 *
 * @param patternName 탐지된 민감 정보 패턴의 이름 (예: "SSN_PATTERN")
 * @param foundText   문서에서 실제로 탐지된 텍스트 (예: "123456-1234567")
 */
public record MatchResult(String patternName, String foundText) {
}