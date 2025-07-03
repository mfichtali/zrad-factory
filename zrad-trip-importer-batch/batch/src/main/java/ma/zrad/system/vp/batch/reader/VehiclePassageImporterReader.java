package ma.zrad.system.vp.batch.reader;

import ma.zrad.system.batch.common.pojo.BatchFileContext;
import ma.zrad.system.batch.common.records.FileMetadataRecord;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.PassThroughLineMapper;
import org.springframework.core.io.FileSystemResource;

public class VehiclePassageImporterReader extends FlatFileItemReader<String> {

    public VehiclePassageImporterReader(BatchFileContext batchFileContext, FileMetadataRecord fileMetadataRecord) {
        String fileInputReader = String.format("%s/%s", batchFileContext.getInputDir(), fileMetadataRecord.fileOriginalName());
        this.setResource(new FileSystemResource(fileInputReader));
        this.setLineMapper(new PassThroughLineMapper());
    }

}