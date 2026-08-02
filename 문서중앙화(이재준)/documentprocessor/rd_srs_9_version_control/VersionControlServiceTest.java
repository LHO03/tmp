package documentprocessor.rd_srs_9_version_control;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

class VersionControlServiceTest {

    private final VersionControlService service = new VersionControlService();

    @Test
    void checkIn_shouldCreateInitialVersion() {
        String docId = "doc1";
        byte[] content = "Initial content".getBytes();
        String version = service.checkIn(docId, content, "user1", "Initial check-in");

        assertEquals("1.0.0", version);
        List<Map<String, Object>> history = service.getHistory(docId);
        assertEquals(1, history.size());
        assertEquals("1.0.0", history.get(0).get("version"));
        assertArrayEquals(content, (byte[]) history.get(0).get("content"));
    }

    @Test
    void compareVersions_shouldShowDiff() {
        String docId = "doc2";
        service.checkIn(docId, "line1\nline2".getBytes(), "user1", "v1");
        service.checkIn(docId, "line1\nline3".getBytes(), "user1", "v2");

        String diff = service.compareVersions(docId, "1.0.0", "1.0.1");
        assertTrue(diff.contains("Version 1.0.0: line2"));
        assertTrue(diff.contains("Version 1.0.1: line3"));
    }

    @Test
    void getVersionAtTime_shouldReturnCorrectVersion() throws InterruptedException {
        String docId = "doc3";
        service.checkIn(docId, "content1".getBytes(), "user1", "v1");
        LocalDateTime time1 = LocalDateTime.now();
        Thread.sleep(10); // Ensure timestamp is different
        service.checkIn(docId, "content2".getBytes(), "user1", "v2");

        byte[] content = service.getVersionAtTime(docId, time1.plusNanos(5000000));
        assertArrayEquals("content1".getBytes(), content);
    }

    @Test
    void documentLifecycle_shouldChangeStatus() {
        String docId = "doc4";
        service.checkIn(docId, "content".getBytes(), "user1", "init");
        assertEquals("초안", service.getDocumentStatus(docId));

        service.initiateApprovalWorkflow(docId, List.of("approver1"));
        assertEquals("검토중", service.getDocumentStatus(docId));

        service.disposeDocument(docId);
        assertEquals("폐기", service.getDocumentStatus(docId));

        service.restoreDocument(docId);
        assertEquals("초안", service.getDocumentStatus(docId));
    }
}