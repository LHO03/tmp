package documentprocessor.rd_srs_9_version_control;

/**
 * 버전 변경 이력 로깅 및 이해관계자 알림을 담당합니다. (RD-SRS-9.3)
 */
public class VersionEventService {

    public void logVersionChange(String documentId, String version, String userId, String changeSummary) {
        System.out.println(String.format("Version Change Log: DocId=%s, Version=%s, UserId=%s, Summary='%s'",
                documentId, version, userId, changeSummary));
    }

    public void notifyStakeholders(String docId, String changeEvent) {
        System.out.println("Notification for document " + docId + ": " + changeEvent);
    }
}
