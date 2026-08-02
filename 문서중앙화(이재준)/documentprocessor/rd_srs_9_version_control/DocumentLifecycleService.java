package documentprocessor.rd_srs_9_version_control;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 문서 상태(초안, 승인, 폐기 등) 및 승인 워크플로우를 관리합니다. (RD-SRS-9.2, 9.6, 9.7, 9.8)
 */
public class DocumentLifecycleService {

    private final Map<String, String> documentStatus = new ConcurrentHashMap<>();

    public void setDocumentStatus(String docId, String status) {
        documentStatus.put(docId, status);
        System.out.println("Document " + docId + " status set to: " + status);
    }

    public String getDocumentStatus(String docId) {
        return documentStatus.getOrDefault(docId, "상태 없음");
    }

    public void initiateApprovalWorkflow(String docId, List<String> approvers) {
        System.out.println("Approval workflow initiated for document " + docId + " with approvers: " + approvers);
        setDocumentStatus(docId, "검토중");
    }

    public void disposeDocument(String docId) {
        setDocumentStatus(docId, "폐기");
    }

    public void restoreDocument(String docId) {
        // A more complex logic might be needed to restore to the previous state
        setDocumentStatus(docId, "초안");
    }
}