package documentprocessor.rd_srs_9_version_control;

/**
 * 버전 보관 정책 설정을 담당합니다. (RD-SRS-9.4)
 */
public class VersioningPolicyService {

    public void setVersioningPolicy(int maxVersions, int retentionDays) {
        System.out.println(String.format("Versioning policy set: Max Versions = %d, Retention Days = %d",
                maxVersions, retentionDays));
    }
}
