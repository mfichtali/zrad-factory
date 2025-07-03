package ma.zrad.system.batch.common.enums;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

public enum SourceFileImporterEnum {

    S3, LOCAL, FTP , UNKNOWN;

    public static SourceFileImporterEnum fromCodeOrDefault(String code) {
        if(StringUtils.isBlank(code)) {
            return UNKNOWN;
        }
        return Arrays.stream(SourceFileImporterEnum.values())
                .filter(source -> source.name().equalsIgnoreCase(code))
                .findFirst()
                .orElse(UNKNOWN);
    }
}
