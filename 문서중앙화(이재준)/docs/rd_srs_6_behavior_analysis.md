# 상세 기능 명세: 사용자 행위 분석 (RD-SRS-6.x)

## 1. 개요

`rd_srs_6_behavior_analysis` 패키지는 사용자의 문서 접근 및 활동 로그를 기반으로 비정상 행위를 탐지하고 잠재적 내부 위협을 식별하는 기능을 담당합니다. 실시간 탐지, 장기 패턴 분석, 위협 우선순위 결정, 모델 관리를 포함하는 다층적 접근 방식을 사용합니다.

*   **위치**: `documentprocessor/rd_srs_6_behavior_analysis/`
*   **주요 클래스**: `BehaviorAnalysisService` (Facade) 및 4개의 하위 서비스

## 2. 아키텍처

`BehaviorAnalysisService`는 퍼사드(Facade) 역할을 수행하며, 실제 분석 로직은 다음과 같이 책임이 분리된 4개의 서비스 클래스에 위임됩니다.

*   `RealTimeAnomalyService` (RD-SRS-6.1): 실시간 사용자 활동 로그를 분석하여 이상 행위를 탐지합니다.
*   `PatternAnalysisService` (RD-SRS-6.2): 장기간의 활동 로그를 분석하여 접근 패턴 리포트를 생성합니다.
*   `ThreatPrioritizationService` (RD-SRS-6.3): 탐지된 여러 위협의 우선순위를 동적으로 결정합니다.
*   `ModelManagementService` (RD-SRS-6.4): 이상 행위 탐지 모델을 재학습하고 분석가의 피드백을 반영하여 정확도를 개선합니다.

```mermaid
graph TD
    A[활동 로그] --> B{BehaviorAnalysisService (Facade)};
    B --> C[RealTimeAnomalyService];
    B --> D[PatternAnalysisService];
    B --> E[ThreatPrioritizationService];
    B --> F[ModelManagementService];

    subgraph 분석 및 대응
        C --> G[이상 행위 탐지];
        D --> H[접근 패턴 분석];
        E --> I[위협 우선순위 결정];
    end

    subgraph 모델 관리
        F --> J[모델 재학습 및 피드백 반영];
    end
```

## 3. 주요 기능 및 메서드

### 3.1. `BehaviorAnalysisService`

*   각 하위 서비스의 메서드를 호출하여 전체 행위 분석 워크플로우를 제어합니다. 예를 들어, `detectBehaviorAnomaly` 요청이 오면 `RealTimeAnomalyService`에 위임하는 방식입니다.

### 3.2. `RealTimeAnomalyService`

*   `detectBehaviorAnomaly(...)`: 실시간 활동 로그를 받아 이상 행위 여부를 판단합니다. (예: 업무 시간 외 접근, 과도한 다운로드)

### 3.3. `PatternAnalysisService`

*   `analyzeAccessPatterns(...)`: 특정 기간 동안의 사용자 활동을 종합 분석하여 리포트를 생성합니다. (예: 시간대별 접근 빈도, 주요 접근 문서)

### 3.4. `ThreatPrioritizationService`

*   `prioritizeThreats(...)`: 탐지된 위협 목록을 받아 심각도(`severity`) 등을 기준으로 처리 우선순위를 정렬합니다.

### 3.5. `ModelManagementService`

*   `updateModel(...)`: 새로운 로그 데이터로 모델을 재학습하는 기능을 시뮬레이션합니다.
*   `feedbackFalsePositives(...)`: 분석가의 피드백을 받아 오탐지를 줄이는 데 활용합니다.

## 4. 요구사항 매핑 (RD-SRS-6.x)

| 요구사항 ID | 설명 | 구현 클래스 및 메서드 |
| --- | --- | --- |
| **RD-SRS-6.2** | 사용자 로그 기반 실시간 이상행위 탐지 | `RealTimeAnomalyService.detectBehaviorAnomaly` |
| **RD-SRS-6.3** | 장기간 접근 패턴 분석 및 리포팅 | `PatternAnalysisService.analyzeAccessPatterns` |
| **RD-SRS-6.4** | 위험도 기반 위협 우선순위 부여 | `ThreatPrioritizationService.prioritizeThreats` |
| **RD-SRS-6.5** | 로그 기반 ML 모델 재학습 | `ModelManagementService.updateModel` |
| **RD-SRS-6.6** | 사용자 피드백 기반 오탐지 감소 학습 | `ModelManagementService.feedbackFalsePositives` |
