package ma.zrad.system.vp.batch.service;

import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicInteger;

@Service
@Getter
public class ImporterStatisticsCollectorService {

    private final AtomicInteger maxLengthErrorLine = new AtomicInteger(0);
    private final AtomicInteger total = new AtomicInteger(0);
    private final AtomicInteger valid = new AtomicInteger(0);
    private final AtomicInteger invalid = new AtomicInteger(0);

    public void addValid(int count) {
        total.addAndGet(count);
        valid.addAndGet(count);
    }

    public void addInvalid(int count) {
        total.addAndGet(count);
        invalid.addAndGet(count);
    }

    public void updateMaxLengthErrorLine(int length) {
        maxLengthErrorLine.updateAndGet(current -> Math.max(current, length));
    }

    public void reset() {
        total.set(0);
        valid.set(0);
        invalid.set(0);
    }
}
