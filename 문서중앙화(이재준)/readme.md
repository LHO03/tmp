# 부경대 서버 사이드 기능 명세 (함수 단위)

본 문서는 문서 보안 시스템 개발을 위한 **서버 사이드 기능 명세서**입니다. 요구사항(RD-SRS-5.x, 6.x, 9.x)을 기준으로 각 기능을 **함수 단위로 구성**하였으며, 향후 Python 기반 구현을 고려하여 설계되었습니다.

---

## 📁 1. 데이터 유출 방지(DLP) 기능 (RD-SRS-5.x)

### ✅ `check_sensitive_data(document: bytes, filename: str) -> dict`

* **적용 요구사항**: RD-SRS-5.1, 5.4, 6.1
* **설명**: 업로드된 문서 내 민감정보 포함 여부를 자동으로 판별
* **기능 구성**:

  * 정규식 탐지 (주민등록번호, 계좌번호 등)
  * 키워드/패턴 탐지
  * 머신러닝 기반 민감도 분석
* **반환값**: `{ has_sensitive: bool, matches: [...], ml_score: float }`

---

### ✅ `check_sensitive_data_on_update(document_diff: str) -> dict`

* **적용 요구사항**: RD-SRS-5.2
* **설명**: 문서 수정 시 변경된 부분에 대해 실시간 민감정보 탐지 수행
* **반환값**: `{ has_sensitive: bool, diff_matches: [...], ml_score: float }`

---

### ✅ `identify_sensitive_patterns(document_text: str) -> List[MatchResult]`

* **적용 요구사항**: RD-SRS-5.4
* **설명**: 다양한 형식의 민감 데이터를 정규식 기반으로 탐지
* **출력 예시**:

```json
[
  { "type": "SSN", "value": "123-45-6789", "start": 120 }
]
```

---

### ✅ `ml_detect_sensitive_data(document: str) -> float`

* **적용 요구사항**: RD-SRS-5.5, 6.1
* **설명**: 머신러닝 분류 모델(BERT 등)을 통한 민감도 판별
* **반환값**: 민감도 점수 (0.0 \~ 1.0)

---

## 🧠 2. 머신러닝 기반 사용자 행위 분석 (RD-SRS-6.x)

### ✅ `detect_behavior_anomaly(user_id: str, activity_log: dict) -> dict`

* **적용 요구사항**: RD-SRS-6.2, 6.7
* **설명**: 사용자 로그 기반 이상행위 탐지 및 실시간 알림
* **반환값**: `{ anomaly: True, type: "access_pattern", confidence: 0.89 }`

---

### ✅ `analyze_access_patterns(user_id: str, start_time, end_time) -> dict`

* **적용 요구사항**: RD-SRS-6.3
* **설명**: 업무 시간 중 문서 접근/열람/이동 패턴 분석
* **반환값**: `{ hourly_access_chart, top_files, avg_duration }`

---

### ✅ `prioritize_threats(threats: List[dict]) -> List[dict]`

* **적용 요구사항**: RD-SRS-6.4
* **설명**: 탐지된 위협에 대해 위험도 기반 우선순위 부여 및 자동 대응
* **반환값**: `[{... threat, "priority": "High" }]`

---

### ✅ `update_model(training_data: pd.DataFrame)`

* **적용 요구사항**: RD-SRS-6.5
* **설명**: 로그 기반 ML 모델 재학습 수행
* **결과**: 로그로 저장

---

### ✅ `feedback_false_positives(data_id: str, label: str)`

* **적용 요구사항**: RD-SRS-6.6
* **설명**: 사용자 피드백을 수집하여 오탐지 감소 학습에 활용

---

## 🗂️ 3. 문서 형상관리 (RD-SRS-9.x)

### ✅ `assign_version(document_id: str) -> str`

* **적용 요구사항**: RD-SRS-9.1, 9.2
* **설명**: 문서 저장/수정 시 자동 버전 넘버 부여

---

### ✅ `log_version_change(document_id: str, user_id: str, change_summary: str)`

* **적용 요구사항**: RD-SRS-9.3
* **설명**: 변경자, 일시, 내용, 사유 기록

---

### ✅ `compare_versions(doc_id: str, ver1: str, ver2: str) -> dict`

* **적용 요구사항**: RD-SRS-9.4
* **설명**: 버전 간 차이점 분석 및 요약 제공

---

### ✅ `get_version_at_time(doc_id: str, timestamp) -> bytes`

* **적용 요구사항**: RD-SRS-9.5
* **설명**: 특정 시점의 문서 버전 반환

---

### ✅ `set_document_status(doc_id: str, status: str)`

* **적용 요구사항**: RD-SRS-9.6
* **설명**: 문서의 현재 상태 설정 (초안, 승인 등)

---

### ✅ `initiate_approval_workflow(doc_id: str, approvers: List[str])`

* **적용 요구사항**: RD-SRS-9.7
* **설명**: 문서 승인 절차를 정의된 흐름에 따라 진행

---

### ✅ `notify_stakeholders(doc_id: str, change_event: str)`

* **적용 요구사항**: RD-SRS-9.9
* **설명**: 이해관계자에게 자동 알림 전송

---

### ✅ `set_versioning_policy(max_versions: int, retention_days: int)`

* **적용 요구사항**: RD-SRS-9.10
* **설명**: 최대 버전 수, 보관 기간 등 정책 설정

---

