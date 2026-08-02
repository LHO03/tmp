# README — DLP (파일 기반 규칙)

본 문서는 DLP 모듈을 **파일 기반 규칙(YAML)** 로 동작하도록 리팩터링한 버전을 기준으로, **기능별 함수 알고리즘**과 **테스트 방법**을 설명합니다. (Java 17, JUnit5, SnakeYAML)

---

## 1) 개요

* **목표**: 정규식(정형)과 키워드(비정형) 규칙을 코드에서 분리하여 운영/확장성 향상
* **핵심 구성**

  * `PatternDetectionService`: 정규식 패턴 기반 매칭 (RD-SRS-5.1, 5.4)
  * `HeuristicDetectionService`: 키워드 기반 민감도 점수 (RD-SRS-5.2, 5.5)
  * `DlpService`: 위 두 결과를 통합하여 민감 여부 판단 (RD-SRS-5)
  * `config/*Repository`: YAML(또는 다른 저장소)로부터 규칙 로드

---

## 2) 디렉토리 구조

```
src/
 ├─ main/
 │   ├─ java/
 │   │   └─ documentprocessor/
 │   │       ├─ core/
 │   │       │   └─ MatchResult.java
 │   │       └─ rd_srs_5_dlp/
 │   │           ├─ DlpService.java
 │   │           ├─ HeuristicDetectionService.java
 │   │           └─ PatternDetectionService.java
 │   └─ resources/
 │       ├─ patterns.yaml
 │       └─ heuristics.yaml
 └─ test/
     └─ java/
         └─ documentprocessor/rd_srs_5_dlp/
             └─ DlpServiceTest.java
```

---

## 3) 구성 파일(YAML) 스키마

### 3.1 `patterns.yaml`

```yaml
patterns:
  - name: SSN_PATTERN           # 필수, 고유
    regex: "\\b\\d{6}[- ]?\\d{7}\\b"  # 필수, Java 정규식 문자열
    severity: high              # 선택: high|medium|low
    tags: [pii, national-id]    # 선택: 태그 배열
```

> **주의:** YAML에서 Java로 정규식을 읽을 때, 역슬래시(`\`)는 이스케이프 처리(`\\`)해야 합니다.

### 3.2 `heuristics.yaml`

```yaml
keywords:
  - token: "대외비"
    weight: 1
  - token: "기밀"
    weight: 1
  - token: "보안"
    weight: 1
  - token: "개인정보"
    weight: 1
  - token: "유출금지"
    weight: 1

scoring:
  normalize_divisor: 10.0  # 키워드 점수 합계를 정규화하는 분모
  threshold: 0.5           # 이 값을 초과하면 민감 데이터로 판정
```

---

## 4) 실행 및 테스트 방법

### 4.1 의존성

*   Java 17+
*   JUnit 5 (테스트용)
*   SnakeYAML (YAML 파싱용)

*프로젝트에 빌드 관리 도구(Maven/Gradle)가 없는 경우, 라이브러리를 직접 다운로드하여 클래스패스에 추가해야 합니다.*

### 4.2 사용 예제

`DlpService`를 초기화하고 `checkSensitiveData` 함수를 호출하여 민감 데이터를 검출할 수 있습니다.

```java
import documentprocessor.rd_srs_5_dlp.DlpService;
import java.util.Map;

public class DlpExample {
    public static void main(String[] args) {
        // 클래스패스에 포함된 YAML 파일을 자동으로 로드합니다.
        DlpService dlpService = new DlpService();

        String text = "이 문서에는 주민번호 123456-1234567과 같은 기밀 정보가 포함되어 있습니다.";
        byte[] content = text.getBytes();

        Map<String, Object> result = dlpService.checkSensitiveData(content, "sample.txt");

        System.out.println("민감 데이터 포함 여부: " + result.get("has_sensitive"));
        System.out.println("정규식 매칭 결과: " + result.get("matches"));
        System.out.println("휴리스틱 점수: " + result.get("ml_score"));
        // 민감 데이터 포함 여부: true
        // 정규식 매칭 결과: [MatchResult[patternName=SSN_PATTERN, matchedText=123456-1234567]]
        // 휴리스틱 점수: 0.1
    }
}
```

### 4.3 테스트 실행

`DlpServiceTest.java`는 JUnit5로 작성된 테스트 케이스를 포함합니다. IDE(IntelliJ, Eclipse)에서 직접 실행하거나, 터미널에서 아래와 같이 JUnit 콘솔 런처를 사용하여 실행할 수 있습니다. (라이브러리 경로 설정 필요)

```bash
# (프로젝트 루트 디렉토리에서 실행)
# 1. 소스 코드 컴파일
javac -d out --source-path src/main/java:src/test/java -cp "lib/junit-jupiter-api.jar:lib/snakeyaml.jar:..." src/test/java/documentprocessor/rd_srs_5_dlp/DlpServiceTest.java

# 2. 테스트 실행
java -jar lib/junit-platform-console-standalone.jar -cp "out:lib/*" --select-class documentprocessor.rd_srs_5_dlp.DlpServiceTest
```

---

## 5) 기능별 함수 & 알고리즘

### 5.1 `DlpService.checkSensitiveData`

이 함수는 DLP 검사의 메인 진입점입니다.

1.  **입력**: 문서의 `byte[]`와 파일명을 받습니다.
2.  **텍스트 변환**: `byte[]`를 UTF-8 문자열로 변환합니다.
3.  **정규식 검사**: `PatternDetectionService`를 호출하여 `patterns.yaml`에 정의된 모든 정규식과 매칭되는 패턴을 찾습니다. (`List<MatchResult>`)
4.  **휴리스틱 검사**: `HeuristicDetectionService`를 호출하여 `heuristics.yaml`의 키워드 출현 빈도를 기반으로 0.0 ~ 1.0 사이의 점수를 계산합니다.
5.  **판정**:
    *   정규식 매칭 결과가 하나라도 있거나,
    *   휴리스틱 점수가 설정된 임계값(`threshold`)을 초과하는 경우,
    *   최종적으로 `has_sensitive: true`로 판정합니다.
6.  **결과**: 판정 결과, 정규식 매칭 목록, 휴리스틱 점수를 `Map`에 담아 반환합니다.

### 5.2 요구사항별 매핑 요약표

| Req     | 목적             | 주요 클래스                                                                | 핵심 함수                                | 입력            | 출력/판정                                | 클라이언트 필요                                                 |
| ------- | -------------- | --------------------------------------------------------------------- | ------------------------------------ | ------------- | ------------------------------------ | -------------------------------------------------------- |
| **5.1** | 저장 문서 자동 구분    | `DlpService` → `PatternDetectionService`, `HeuristicDetectionService` | `checkSensitiveData(byte[], String)` | 문서 바이트        | `{has_sensitive, matches, ml_score}` | **선택(권장)**: 저장/업로드 이벤트를 서버로 전달하면 실시간성↑. 서버 단독 배치/후처리도 가능 |
| **5.2** | 수정 자료 실시간 구분   | `DlpService` → `Pattern/Heuristic`                                    | `checkSensitiveDataOnUpdate(String)` | 변경분(diff) 텍스트 | `{has_sensitive, matches, ml_score}` | **필수(실시간 보장)**: 클라이언트가 수정 이벤트/변경분을 서버로 스트리밍 또는 푸시 필요     |
| **5.4** | 다양한 형식의 식별     | `PatternDetectionService`                                             | `identifySensitivePatterns(String)`  | 텍스트           | `List<MatchResult>`                  | **불필요**(서버 검출). 선택: 클라 사전검사로 UX 개선                       |
| **5.5** | 고급 탐지(ML/휴리스틱) | `HeuristicDetectionService`                                           | `mlDetectSensitiveData(String)`      | 텍스트           | `double ml_score`                    | **불필요**(서버 추론). 선택: 클라 로컬 사전검사 가능                        |

### 5.3 요구사항별 알고리즘 상세

#### 5.3.1 RD-SRS-5.4: 다양한 형식의 식별 (정규식 기반)

`PatternDetectionService.identifySensitivePatterns(String)`

1.  **규칙 로드**: `patterns.yaml` 파일에서 정규식 규칙 목록을 로드합니다. 각 규칙은 `name`, `regex`, `severity` 등을 포함합니다.
2.  **정규식 컴파일**: 로드된 각 `regex` 문자열을 `java.util.regex.Pattern` 객체로 컴파일하여 리스트에 저장합니다. 이 과정은 서비스 초기화 시 한 번만 수행됩니다.
3.  **매칭 수행**:
    *   입력된 텍스트에 대해 컴파일된 모든 정규식 패턴을 순회합니다.
    *   `Matcher.find()`를 사용하여 텍스트 내에서 각 패턴과 일치하는 모든 부분을 찾습니다.
    *   일치하는 경우, `MatchResult` 객체(패턴 이름, 매칭된 텍스트)를 생성하여 결과 리스트에 추가합니다.
4.  **결과 반환**: 탐지된 모든 `MatchResult`의 리스트를 반환합니다.

---

#### 5.3.2 RD-SRS-5.5: 고급 탐지 (휴리스틱 기반)

`HeuristicDetectionService.mlDetectSensitiveData(String)`

1.  **규칙 로드**: `heuristics.yaml` 파일에서 키워드 및 점수 계산 규칙(`scoring`)을 로드합니다.
2.  **텍스트 정규화**: 입력된 텍스트 전체를 소문자로 변환하여 비교 연산이 대소문자에 영향을 받지 않도록 합니다.
3.  **키워드 카운팅**:
    *   로드된 모든 키워드(`token`)를 순회합니다.
    *   `String.indexOf()`를 사용하여 텍스트 내에서 각 키워드가 몇 번 나타나는지 계산합니다.
    *   각 키워드의 가중치(`weight`)를 곱한 값을 누적하여 총점을 계산합니다.
4.  **점수 정규화**:
    *   계산된 총점을 `scoring.normalize_divisor` 값으로 나눕니다.
    *   결과값이 1.0을 초과하지 않도록 `Math.min(1.0, ...)`을 적용합니다.
5.  **결과 반환**: 최종 계산된 민감도 점수(0.0 ~ 1.0)를 반환합니다.

---

#### 5.3.3 RD-SRS-5.1 & 5.2: 민감 데이터 통합 탐지

`DlpService.checkSensitiveData(byte[], String)`

1.  **입력 처리**: `byte[]` 형태의 문서를 `String`으로 변환합니다.
2.  **정규식 검사 호출**: `PatternDetectionService`를 호출하여 정규식 기반 탐지를 수행하고, `List<MatchResult>`를 얻습니다. (→ 5.3.1 참고)
3.  **휴리스틱 검사 호출**: `HeuristicDetectionService`를 호출하여 키워드 기반 점수를 계산하고, `double` 타입의 점수를 얻습니다. (→ 5.3.2 참고)
4.  **최종 판정**:
    *   정규식 매칭 결과 리스트가 비어있지 않거나 (`!regexMatches.isEmpty()`),
    *   휴리스틱 점수가 `heuristics.yaml`에 정의된 임계값(`threshold`)을 초과하는 경우 (`mlScore > threshold`),
    *   문서에 민감 정보가 포함된 것(`has_sensitive: true`)으로 최종 판단합니다.
5.  **결과 종합**: 최종 판정 결과(`boolean`), 정규식 매칭 목록(`List`), 휴리스틱 점수(`double`)를 `Map` 객체에 담아 반환합니다.

