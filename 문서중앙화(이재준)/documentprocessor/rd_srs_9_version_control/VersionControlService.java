package documentprocessor.rd_srs_9_version_control;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

/**
 * 문서의 생성, 수정, 폐기 등 전체 생명주기에 걸쳐 변경 이력을 체계적으로 관리하고,
 * 문서의 버전을 제어하는 기능을 총괄하는 서비스 클래스입니다. (RD-SRS-9)
 */
public class VersionControlService {

    private final VersioningService versioningService;
    private final DocumentLifecycleService lifecycleService;
    private final VersionEventService eventService;
    private final VersioningPolicyService policyService;

    public VersionControlService() {
        this.versioningService = new VersioningService();
        this.lifecycleService = new DocumentLifecycleService();
        this.eventService = new VersionEventService();
        this.policyService = new VersioningPolicyService();
    }

    public String checkIn(String documentId, byte[] content, String userId, String changeSummary) {
        String newVersion = versioningService.assignVersion(documentId);
        eventService.logVersionChange(documentId, newVersion, userId, changeSummary);

        versioningService.addVersion(documentId, Map.of(
                "version", newVersion,
                "timestamp", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                "userId", userId,
                "changeSummary", changeSummary,
                "content", content
        ));

        eventService.notifyStakeholders(documentId, "Document " + documentId + " checked in with version " + newVersion);

        if (lifecycleService.getDocumentStatus(documentId).equals("상태 없음")) {
            lifecycleService.setDocumentStatus(documentId, "초안");
        } else if (lifecycleService.getDocumentStatus(documentId).equals("승인")) {
            lifecycleService.setDocumentStatus(documentId, "수정본_초안");
        }

        return newVersion;
    }

    public byte[] checkOut(String documentId, String version) {
        return versioningService.checkOut(documentId, version);
    }

    public List<Map<String, Object>> getHistory(String documentId) {
        return versioningService.getHistory(documentId);
    }

    public String compareVersions(String docId, String ver1, String ver2) {
        return versioningService.compareVersions(docId, ver1, ver2);
    }

    public byte[] getVersionAtTime(String docId, LocalDateTime timestamp) {
        return versioningService.getVersionAtTime(docId, timestamp);
    }

    public void setDocumentStatus(String docId, String status) {
        lifecycleService.setDocumentStatus(docId, status);
    }

    public String getDocumentStatus(String docId) {
        return lifecycleService.getDocumentStatus(docId);
    }

    public void initiateApprovalWorkflow(String docId, List<String> approvers) {
        lifecycleService.initiateApprovalWorkflow(docId, approvers);
    }

    public void disposeDocument(String docId) {
        lifecycleService.disposeDocument(docId);
    }

    public void restoreDocument(String docId) {
        lifecycleService.restoreDocument(docId);
    }

    public void setVersioningPolicy(int maxVersions, int retentionDays) {
        policyService.setVersioningPolicy(maxVersions, retentionDays);
    }
}