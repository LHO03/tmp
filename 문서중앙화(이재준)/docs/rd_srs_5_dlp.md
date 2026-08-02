# 상세 기능 명세: 데이터 유출 방지 (RD-SRS-5.x)

## 1. 개요

`rd_srs_5_dlp` 패키지는 문서 내 민감 정보 탐지 및 유출 방지 기능을 담당합니다. 정형화된 패턴 탐지(RD-SRS-5.1)와 비정형 데이터 분석(RD-SRS-5.2)을 결합하여 조직의 정보 자산을 보호하고 규제를 준수합니다.

*   **위치**: `documentprocessor/rd_srs_5_dlp/`
*   **주요 클래스**: `DlpService` (Facade), `PatternDetectionService`, `HeuristicDetectionService`

## 2. 아키텍처

`DlpService`는 퍼사드(Facade) 역할을 수행하며, 실제 탐지 로직은 다음과 같이 분리된 서비스 클래스에 위임됩니다.

*   `PatternDetectionService` (RD-SRS-5.1): 정규식을 사용하여 주민등록번호, 계좌번호 등 구조가 명확한 정형 데이터를 신속하고 정확하게 탐지합니다.
*   `HeuristicDetectionService` (RD-SRS-5.2): '대외비', '기밀'과 같은 민감 키워드의 출현 빈도를 기반으로 점수를 계산하여 비정형 데이터나 문맥에 따른 민감도를 분석합니다.

```mermaid
graph TD
    A[문서 입력] --> B{DlpService (Facade)};
    B --> C[PatternDetectionService];
    B --> D[HeuristicDetectionService];
    C --> E[정규식 기반 탐지 결과];
    D --> F[키워드 기반 민감도 점수];
    E --> G{결과 종합};
    F --> G;
    G --> H[최종 결과 반환];
```

## 3. 주요 기능 및 메서드

### 3.1. `DlpService`

*   `checkSensitiveData(byte[] document, String filename)`: 업로드된 문서 전체를 대상으로 민감 정보 스캔을 총괄합니다. `PatternDetectionService`와 `HeuristicDetectionService`를 호출하여 결과를 종합한 후 최종 탐지 결과를 반환합니다.
*   `checkSensitiveDataOnUpdate(String documentDiff)`: 문서 수정 시 변경된 내용(`documentDiff`)만을 대상으로 효율적인 실시간 탐지를 수행합니다.

### 3.2. `PatternDetectionService`

*   `identifySensitivePatterns(String documentText)`: 다양한 형식의 민감 데이터를 정규식 기반으로 탐지합니다.
    *   **탐지 패턴**: 주민등록번호, 계좌번호, 휴대폰 번호, 이메일 주소, 신용카드 번호 등.

### 3.3. `HeuristicDetectionService`

*   `mlDetectSensitiveData(String documentText)`: 지정된 민감 키워드('대외비', '기밀' 등)의 출현 빈도를 기반으로 민감도 점수를 계산합니다. 향후 실제 머신러닝 모델과 연동하여 고도화될 수 있습니다.

## 4. 요구사항 매핑 (RD-SRS-5.x)

| 요구사항 ID | 설명 | 구현 클래스 및 메서드 |
| --- | --- | --- |
| **RD-SRS-5.1** | 문서 업로드 시 자동 민감정보 탐지 | `DlpService.checkSensitiveData` |
| **RD-SRS-5.2** | 문서 수정 시 변경분에 대한 실시간 탐지 | `DlpService.checkSensitiveDataOnUpdate` |
| **RD-SRS-5.4** | 정규식을 이용한 정형 데이터 탐지 | `PatternDetectionService.identifySensitivePatterns` |
| **RD-SRS-5.5** | 머신러닝/키워드 기반 비정형 데이터 분석 | `HeuristicDetectionService.mlDetectSensitiveData` |
