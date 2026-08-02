package documentprocessor.rd_srs_5_dlp;

import documentprocessor.core.MatchResult;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

class DlpServiceTest {

    private final DlpService dlpService = new DlpService(); // classpath의 YAML 사용

    /**
     * 테스트 리소스 디렉토리의 파일을 읽어 byte[]로 반환하는 헬퍼 함수
     */
    private byte[] readTestFile(Path filePath) throws IOException {
        return Files.readAllBytes(filePath);
    }

    @TestFactory
    @DisplayName("[파일] 리소스 폴더의 모든 파일을 동적으로 테스트")
    Stream<DynamicTest> dynamicTestsFromResourceFiles() throws IOException {
        Path resourceDirectory = Paths.get("src", "test", "resources");

        return Files.walk(resourceDirectory)
                .filter(Files::isRegularFile)
                .map(filePath -> {
                    String fileName = filePath.getFileName().toString();
                    return dynamicTest("파일 테스트: " + fileName, () -> {
                        byte[] content = readTestFile(filePath);
                        Map<String, Object> result = dlpService.checkSensitiveData(content, fileName);

                        if (fileName.startsWith("sensitive_")) {
                            assertTrue((Boolean) result.get("has_sensitive"), fileName + " 파일은 'sensitive'로 탐지되어야 합니다.");
                        } else if (fileName.startsWith("clean_")) {
                            assertFalse((Boolean) result.get("has_sensitive"), fileName + " 파일은 'clean'으로 탐지되어야 합니다.");
                        }
                        // 파일명 규칙에 맞지 않는 파일은 무시하거나, 필요시 예외 처리 추가 가능
                    });
                });
    }

    @Test
    @DisplayName("[정규식] 주민등록번호가 포함된 경우를 탐지해야 한다")
    void checkSensitiveData_shouldDetectSsn() {
        byte[] content = "내 주민번호는 123456-1234567 입니다.".getBytes();
        Map<String, Object> result = dlpService.checkSensitiveData(content, "test.txt");

        assertTrue((Boolean) result.get("has_sensitive"));
        List<MatchResult> matches = (List<MatchResult>) result.get("matches");
        assertEquals(1, matches.size());
        assertEquals("SSN_PATTERN", matches.get(0).patternName());
        assertEquals("123456-1234567", matches.get(0).matchedText());
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "내 신용카드는 4999-1234-5678-9012 입니다.", // CREDIT_CARD_PATTERN
        "계좌번호는 123-45-678901 입니다.",       // ACCOUNT_PATTERN
        "연락처는 010-1234-5678 입니다."         // PHONE_PATTERN
    })
    @DisplayName("[정규식] 다양한 민감 데이터 패턴을 탐지해야 한다")
    void testVariousPatterns(String text) {
        Map<String, Object> result = dlpService.checkSensitiveData(text.getBytes(), "test.txt");
        assertTrue((Boolean) result.get("has_sensitive"));
        assertFalse(((List<MatchResult>) result.get("matches")).isEmpty());
    }

    @Test
    @DisplayName("[성능] 민감 데이터 검사는 100ms 안에 완료되어야 한다")
    void testPerformance() {
        assertTimeoutPreemptively(Duration.ofMillis(100), () -> {
            byte[] content = Files.readAllBytes(Paths.get("src", "test", "resources", "sensitive_mixed.txt"));
            dlpService.checkSensitiveData(content, "performance_test.txt");
        }, "DLP 검사가 지정된 시간(100ms)을 초과했습니다.");
    }
}