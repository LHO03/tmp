package documentprocessor.rd_srs_5_dlp;

import org.junit.jupiter.api.Test;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

class DlpServiceTest {

    private final DlpService dlpService = new DlpService();

    @Test
    void checkSensitiveData_shouldDetectSsn() {
        // Given
        byte[] content = "My SSN is 123456-1234567.".getBytes();

        // When
        Map<String, Object> result = dlpService.checkSensitiveData(content, "test.txt");

        // Then
        assertTrue((Boolean) result.get("has_sensitive"));
    }

    @Test
    void checkSensitiveData_shouldNotDetectAnything() {
        // Given
        byte[] content = "This is a normal text.".getBytes();

        // When
        Map<String, Object> result = dlpService.checkSensitiveData(content, "test.txt");

        // Then
        assertFalse((Boolean) result.get("has_sensitive"));
    }

    @Test
    void checkSensitiveDataOnUpdate_shouldDetectEmail() {
        // Given
        String diff = "Added email: test@example.com";

        // When
        Map<String, Object> result = dlpService.checkSensitiveDataOnUpdate(diff);

        // Then
        assertTrue((Boolean) result.get("has_sensitive"));
    }
}
