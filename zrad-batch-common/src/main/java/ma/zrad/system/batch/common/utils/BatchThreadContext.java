package ma.zrad.system.batch.common.utils;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.Map;

import static ma.zrad.system.batch.common.utils.BatchConstantUtils.BATCH_PROCESSING_DATE;
import static ma.zrad.system.batch.common.utils.BatchConstantUtils.BATCH_PROCESSING_YEAR_MONTH_DATE;

public class BatchThreadContext {

    private static final ThreadLocal<Map<String, Object>> context = ThreadLocal.withInitial(HashMap::new);

    public static void put(String key, Object value) {
        context.get().put(key, value);
    }
    public static Object get(String key) {
        return context.get().get(key);
    }

    public static <T> T get(String key, Class<T> clazz) {
        return clazz.cast(context.get().get(key));
    }
    public static boolean containsKey(String key) {
        return context.get().containsKey(key);
    }

    public static Map<String, Object> getAll() {
        return context.get();
    }

    public static void clear() {
        context.remove();
    }

    public static void setBatchDate(LocalDate date) {
        put(BATCH_PROCESSING_DATE, date);
    }

    public static void setBatchYearMonthDate(YearMonth date) {
        put(BATCH_PROCESSING_YEAR_MONTH_DATE, date);
    }

    public static LocalDate getBatchDate() {
        Object val = get(BATCH_PROCESSING_DATE);
        return (val instanceof LocalDate) ? (LocalDate) val : null;
    }

    public static YearMonth getBatchYearMonthDate() {
        Object val = get(BATCH_PROCESSING_YEAR_MONTH_DATE);
        return (val instanceof YearMonth) ? (YearMonth) val : null;
    }
}
