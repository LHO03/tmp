# 문서 중앙화 시스템: 기능 명세 및 프로세스 설명 (v2)

## 1. 개요

본 문서는 `documentprocessor` 시스템의 핵심 기능과 전체적인 처리 흐름을 **요구사항 기반으로 재구성된 폴더 구조**에 맞춰 설명합니다. 이 시스템은 문서의 내용을 분석, 처리하고, 민감 정보를 탐지하며, 사용자 행위 분석 및 문서 버전 관리를 통해 지능적인 문서 보안 및 관리를 목표로 합니다.

## 2. 시스템 아키텍처 및 폴더 구조

시스템의 아키텍처는 요구사항 명세(RD-SRS)와 직접적으로 매핑되어 유지보수성과 추적성을 극대화합니다. 각 주요 기능은 요구사항 번호에 따라 별도의 패키지로 분리되어 있습니다.

```
/Users/ijaejun/Desktop/문서중앙화/
├───.DS_Store
├───readme.md
├───System_Documentation.md
├───run_tests.sh
├───sources.txt
├───lib/ # 외부 라이브러리 (JUnit 등) 저장
│   ├───junit-jupiter-api-5.9.1.jar
│   ├───junit-jupiter-engine-5.9.1.jar
│   └───junit-platform-console-standalone-1.9.1.jar
├───out/ # 컴파일된 클래스 파일 저장
├───docs/ # 시스템 문서화 파일
│   ├───.DS_Store
│   ├───core_services.md
│   ├───rd_srs_5_dlp.md
│   ├───rd_srs_6_behavior_analysis.md
│   └───rd_srs_9_version_control.md
└───documentprocessor/ # 핵심 비즈니스 로직 패키지
    ├───.DS_Store
    ├───core/ # 공통 핵심 기능
    │   ├───Document.java
    │   ├───DocumentProcessor.java
    │   ├───DocumentType.java
    │   ├───MatchResult.java
    │   ├───model/ # 데이터 모델
    │   │   └───ProcessedDocument.java
    │   └───processing/ # 기본 문서 처리 로직
    │       ├───DocumentCategorizer.java
    │       ├───KeywordExtractor.java
    │       └───TextExtractor.java
    ├───rd_srs_5_dlp/ # 데이터 유출 방지 (DLP) 기능 (RD-SRS-5.x)
    │   ├───DlpService.java
    │   ├───HeuristicDetectionService.java
    │   └───PatternDetectionService.java
    ├───rd_srs_6_behavior_analysis/ # 사용자 행위 분석 기능 (RD-SRS-6.x)
    │   ├───BehaviorAnalysisService.java
    │   ├───ModelManagementService.java
    │   ├───PatternAnalysisService.java
    │   ├───RealTimeAnomalyService.java
    │   └───ThreatPrioritizationService.java
    └───rd_srs_9_version_control/ # 문서 형상 관리 기능 (RD-SRS-9.x)
        ├───DocumentLifecycleService.java
        ├───VersionControlService.java
        ├───VersionEventService.java
        ├───VersioningPolicyService.java
        └───VersioningService.java
```

### 폴더별 설명:

*   `.DS_Store`: macOS에서 자동으로 생성되는 숨김 파일입니다.
*   `readme.md`: 프로젝트에 대한 간략한 소개 및 사용법을 담고 있습니다.
*   `System_Documentation.md`: 본 문서로, 시스템의 상세 기능 명세 및 아키텍처를 설명합니다.
*   `run_tests.sh`: Java 소스 코드를 컴파일하고 JUnit 테스트를 실행하는 셸 스크립트입니다.
*   `sources.txt`: `run_tests.sh` 스크립트에서 컴파일할 Java 소스 파일 목록을 포함합니다.
*   `lib/`: 프로젝트에서 사용하는 외부 라이브러리(예: JUnit) JAR 파일들이 저장되는 디렉토리입니다.
*   `out/`: Java 소스 코드가 컴파일된 `.class` 파일들이 저장되는 출력 디렉토리입니다.
*   `docs/`: 시스템의 다양한 기능에 대한 상세 문서(마크다운 파일)를 포함합니다.
    *   `core_services.md`: 핵심 공통 기능에 대한 설명을 담고 있습니다.
    *   `rd_srs_5_dlp.md`: 데이터 유출 방지(DLP) 기능에 대한 상세 설명을 담고 있습니다.
    *   `rd_srs_6_behavior_analysis.md`: 사용자 행위 분석 기능에 대한 상세 설명을 담고 있습니다.
    *   `rd_srs_9_version_control.md`: 문서 형상 관리 기능에 대한 상세 설명을 담고 있습니다.
*   `documentprocessor/`: 시스템의 주요 비즈니스 로직을 포함하는 최상위 Java 패키지입니다.
    *   `core/`: 시스템 전반에 걸쳐 사용되는 공통 핵심 기능 및 유틸리티 클래스들을 포함합니다.
        *   `model/`: 시스템 내에서 사용되는 데이터 모델(DTO) 클래스들을 정의합니다.
        *   `processing/`: 문서의 텍스트 추출, 분류, 키워드 추출 등 기본적인 문서 처리 로직을 구현하는 클래스들을 포함합니다.
    *   `rd_srs_5_dlp/`: **데이터 유출 방지(DLP)** 관련 기능을 구현하는 패키지입니다. `DlpService` (Facade)와 세분화된 `PatternDetectionService`, `HeuristicDetectionService`를 포함합니다.
    *   `rd_srs_6_behavior_analysis/`: **사용자 행위 분석** 관련 기능을 구현하는 패키지입니다. `BehaviorAnalysisService` (Facade)와 세분화된 `RealTimeAnomalyService`, `PatternAnalysisService`, `ThreatPrioritizationService`, `ModelManagementService`를 포함합니다.
    *   `rd_srs_9_version_control/`: **문서 형상 관리** 관련 기능을 구현하는 패키지입니다. `VersionControlService` (Facade)와 세분화된 `VersioningService`, `DocumentLifecycleService`, `VersionEventService`, `VersioningPolicyService`를 포함합니다.

## 3. 주요 기능 및 프로세스

### 3.1. 핵심 공통 기능 (Core Functionality)

모든 기능의 기반이 되는 문서 처리 및 분석 파이프라인입니다.

*   **관련 모듈**: `documentprocessor.core`
*   **프로세스 흐름**:
    1.  **텍스트 추출 (`TextExtractor`)**: `Document` 객체에서 순수 텍스트 콘텐츠를 추출합니다. 현재는 간단한 텍스트 추출 로직을 시뮬레이션하며, 실제 구현에서는 Apache Tika와 같은 외부 라이브러리와 연동하여 지원 포맷을 확장할 수 있습니다.
    2.  **문서 분류 (`DocumentCategorizer`)**: 추출된 텍스트의 내용을 분석하여 문서를 'Finance', 'Legal', 'General' 등의 사전에 정의된 카테고리로 분류합니다. 현재는 키워드 기반의 단순한 분류 로직을 사용합니다.
    3.  **키워드 추출 (`KeywordExtractor`)**: 문서의 핵심 내용을 대표하는 주요 단어 또는 구(Phrase)를 추출합니다. 현재는 간단한 공백 기반 단어 분리 및 불용어(Stopwords) 필터링 로직을 사용하며, TF-IDF나 TextRank 같은 알고리즘을 적용하여 정확도를 높일 수 있습니다.
*   **핵심 객체**:
    *   `Document`: 시스템이 처리할 원본 문서를 나타내는 클래스입니다. 각 문서는 고유 ID, 바이너리 내용, 그리고 문서의 종류(`DocumentType` enum)를 가집니다.
    *   `ProcessedDocument`: 텍스트 추출, 분류, 키워드 추출이 완료된 문서를 나타내는 데이터 모델 클래스입니다. 추출된 텍스트, 분류된 카테고리, 핵심 키워드 목록을 포함합니다.

### 3.2. 데이터 유출 방지 (RD-SRS-5.x)

문서 내 민감 정보 포함 여부를 검사하여 정보 유출을 사전에 차단합니다.

*   **관련 모듈**: `documentprocessor.rd_srs_5_dlp`
*   **핵심 클래스**: `DlpService`
*   **상세 기능**:
    *   **정규식 기반 패턴 탐지 (`identifySensitivePatterns`)**: 주민등록번호, 계좌번호, 휴대폰 번호, 이메일 주소, 신용카드 번호 등 정형화된 민감 정보를 정규식을 사용하여 신속하고 정확하게 탐지합니다.
    *   **키워드 기반 민감도 분석 (`mlDetectSensitiveData`)**: '대외비', '기밀', '보안', '개인정보', '유출금지' 등 지정된 민감 키워드의 출현 빈도를 기반으로 문서의 민감도를 정량적으로 평가합니다.
    *   **통합 분석 (`checkSensitiveData`, `checkSensitiveDataOnUpdate`)**: 패턴 탐지 결과와 민감도 분석 결과를 종합하여 최종적으로 문서의 민감 정보 포함 여부를 판단합니다. 새로 업로드된 문서 전체 또는 변경된 내용(`documentDiff`)만을 대상으로 스캔할 수 있습니다.

### 3.3. 사용자 행위 분석 (RD-SRS-6.x)

사용자의 활동 로그를 기반으로 비정상적인 활동을 탐지하고 잠재적 위협을 분석합니다.

*   **관련 모듈**: `documentprocessor.rd_srs_6_behavior_analysis`
*   **핵심 클래스**: `BehaviorAnalysisService`
*   **상세 기능**:
    *   **이상 행위 탐지 (`detectBehaviorAnomaly`)**: 실시간으로 발생하는 사용자 활동 로그를 분석하여 이상 행위 여부를 판단합니다. 현재는 특정 활동(`unusual_access`)에 대해 이상 행위로 간주하는 단순 예시로 구현되어 있으며, 향후 ML 모델이나 정교한 규칙 기반 시스템으로 확장될 수 있습니다.
    *   **접근 패턴 분석 (`analyzeAccessPatterns`)**: 특정 기간 동안의 사용자 활동 로그를 종합적으로 분석하여 장기적인 접근 패턴(시간대별 접근 빈도, 가장 자주 접근한 문서, 평균 열람 시간 등)을 도출하고 리포트를 생성합니다.
    *   **위협 우선순위 결정 (`prioritizeThreats`)**: 탐지된 여러 위협 이벤트들의 심각도('High', 'Medium', 'Low')를 종합하여 처리해야 할 위협의 우선순위를 동적으로 결정합니다.
    *   **모델 관리 및 피드백 (`updateModel`, `feedbackFalsePositives`)**: 새로운 활동 로그 데이터를 사용하여 이상 행위 탐지 모델을 주기적으로 재학습하고, 분석가의 오탐 피드백을 수집하여 모델 정확도를 개선하는 기능을 시뮬레이션합니다.

### 3.4. 문서 형상 관리 (RD-SRS-9.x)

문서의 전체 생명주기에 걸쳐 변경 이력을 체계적으로 관리합니다.

*   **관련 모듈**: `documentprocessor.rd_srs_9_version_control`
*   **핵심 클래스**: `VersionControlService`
*   **상세 기능**:
    *   **버전 관리 (`checkIn`, `checkOut`, `assignVersion`, `logVersionChange`)**: 문서 수정 시마다 버전을 자동으로 부여하고 변경 이력을 기록합니다. `checkIn` 시 새 버전 번호를 할당하고 변경 이력을 기록하며, 문서 상태를 '초안' 또는 '수정본_초안'으로 설정합니다.
    *   **이력 추적 및 비교 (`getHistory`, `compareVersions`, `getVersionAtTime`)**: 특정 문서의 전체 버전 변경 이력 목록을 조회하고, 두 버전 간의 내용 차이를 분석하며, 특정 시점의 문서 버전을 반환하는 기능을 제공합니다.
    *   **상태 관리 및 워크플로우 (`setDocumentStatus`, `initiateApprovalWorkflow`, `notifyStakeholders`, `getDocumentStatus`)**: 문서 상태를 '초안', '검토 중', '승인', '폐기' 등으로 변경하고 승인 워크플로우를 시작하며, 주요 변경 이벤트 발생 시 이해관계자에게 알림을 전송합니다. 문서의 현재 상태를 조회할 수 있습니다.
    *   **정책 설정 (`setVersioningPolicy`)**: 최대 보관 버전 수, 버전 보관 기간 등 형상 관리 정책을 설정합니다.

## 4. 전체 워크플로우 예시: "사용자가 계약서 파일을 업로드하는 경우"

1.  **(사용자)**: `계약서_v1.docx` 파일을 시스템에 업로드합니다.
2.  **[RD-SRS-9]** `VersionControlService`가 문서를 `checkIn`하여 첫 버전(`1.0.0`)을 부여하고 이력을 기록합니다.
3.  **[RD-SRS-5]** `DlpService`가 `checkSensitiveData`를 호출하여 문서 내용을 스캔합니다. '계좌번호' 패턴을 탐지하고, 민감도 점수를 '0.8'로 평가하여 결과를 반환합니다.
4.  **[Core]** `DocumentProcessor`가 `processDocument`를 호출하여 텍스트 추출, `Legal` 카테고리 분류, 핵심 키워드 추출을 수행합니다.
5.  **(시스템)**: DLP 분석 결과 민감도가 높으므로, 해당 문서를 '주의' 상태로 설정하고 보안 담당자에게 알림을 전송합니다.
6.  **[RD-SRS-6]** `BehaviorAnalysisService`가 이 모든 활동을 사용자 활동 로그에 기록하여 향후 이상 행위 분석의 기반 데이터로 활용합니다.
