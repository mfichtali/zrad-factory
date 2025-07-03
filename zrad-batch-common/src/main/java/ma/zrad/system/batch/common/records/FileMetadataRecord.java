package ma.zrad.system.batch.common.records;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public record FileMetadataRecord(
        String fileOriginalName,
        String regionCode,
        String sectionRoadCode,
        LocalDate dateDetection,
        String sequence
) implements Serializable {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");

    public static FileMetadataRecord from(
            String fileOriginalName,
            String codeSectionRoad,
            String codeRadarDetection,
            String rawDateDetection,
            String sequence
    ) {
        LocalDate parsedDate = LocalDate.parse(rawDateDetection, FORMATTER);
        return new FileMetadataRecord(fileOriginalName, codeSectionRoad, codeRadarDetection, parsedDate, sequence);
    }
}
