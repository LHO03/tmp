package documentprocessor.rd_srs_9_version_control;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 버전 생성, 조회, 비교 등 핵심 버전 관리 기능을 담당합니다. (RD-SRS-9.1, 9.4, 9.5)
 */
public class VersioningService {

    private final Map<String, List<Map<String, Object>>> documentVersions = new ConcurrentHashMap<>();

    public String assignVersion(String documentId) {
        List<Map<String, Object>> versions = documentVersions.get(documentId);
        if (versions == null || versions.isEmpty()) {
            return "1.0.0";
        } else {
            String lastVersion = (String) versions.get(versions.size() - 1).get("version");
            String[] parts = lastVersion.split("\\.");
            int patch = Integer.parseInt(parts[2]);
            return parts[0] + "." + parts[1] + "." + (patch + 1);
        }
    }

    public byte[] checkOut(String documentId, String version) {
        return getVersionContent(documentId, version).orElse(null);
    }

    public List<Map<String, Object>> getHistory(String documentId) {
        return documentVersions.getOrDefault(documentId, new ArrayList<>());
    }

    public String compareVersions(String docId, String ver1, String ver2) {
        String content1 = new String(getVersionContent(docId, ver1).orElse(new byte[0]));
        String content2 = new String(getVersionContent(docId, ver2).orElse(new byte[0]));

        // Simple diff logic
        List<String> lines1 = content1.lines().collect(Collectors.toList());
        List<String> lines2 = content2.lines().collect(Collectors.toList());
        StringBuilder diff = new StringBuilder();

        for (int i = 0; i < Math.max(lines1.size(), lines2.size()); i++) {
            String line1 = i < lines1.size() ? lines1.get(i) : "";
            String line2 = i < lines2.size() ? lines2.get(i) : "";
            if (!line1.equals(line2)) {
                diff.append(String.format("Version %s: %s\n", ver1, line1));
                diff.append(String.format("Version %s: %s\n", ver2, line2));
            }
        }
        return diff.toString();
    }

    public byte[] getVersionAtTime(String docId, LocalDateTime timestamp) {
        List<Map<String, Object>> versions = getHistory(docId);
        Optional<Map<String, Object>> foundVersion = versions.stream()
                .filter(v -> LocalDateTime.parse((String)v.get("timestamp"), DateTimeFormatter.ISO_LOCAL_DATE_TIME).isBefore(timestamp))
                .reduce((first, second) -> second); // Get the last version before the timestamp

        return foundVersion.map(v -> (byte[]) v.get("content")).orElse(null);
    }

    public void addVersion(String documentId, Map<String, Object> versionData) {
        documentVersions.computeIfAbsent(documentId, k -> new ArrayList<>()).add(versionData);
    }

    private Optional<byte[]> getVersionContent(String documentId, String version) {
        return getHistory(documentId).stream()
                .filter(v -> v.get("version").equals(version))
                .findFirst()
                .map(v -> (byte[]) v.get("content"));
    }
}