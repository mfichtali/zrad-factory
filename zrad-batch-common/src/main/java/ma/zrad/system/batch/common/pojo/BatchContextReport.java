package ma.zrad.system.batch.common.pojo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Accessors(chain = true)
@NoArgsConstructor
@Getter
@Setter
public class BatchContextReport implements Serializable {
    private UUID eventId;
    private Instant start;
    private Instant end;
    private int readCount;
    private List<String> errors;

}
