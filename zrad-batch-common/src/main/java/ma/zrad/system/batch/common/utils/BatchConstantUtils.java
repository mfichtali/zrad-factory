package ma.zrad.system.batch.common.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BatchConstantUtils {

	public static final String BATCH_PROCESSING_DATE = "batchProcessingDate";
	public static final String BATCH_PROCESSING_YEAR_MONTH_DATE = "batchProcessingYearMonthDate";
	public static final String BATCH_PROCESSING_GRANULARITY = "batchProcessingGranularity";
	public static final String BATCH_METRICS_MODE_CTX = "batchMetricsModeCtx";
	public static final String BATCH_METRICS_LOCAL_DATE_CTX = "batchMetricsLocalDateCtx";
	public static final String BATCH_METRICS_YEAR_MONTH_CTX = "batchMetricsYearMonthCtx";

	public static final String BATCH_CTX_VPI_EVENT_ID		= "eventVpiCtxIdentifier";
	public static final String BATCH_CTX_TRIP_EVENT_ID = "eventTripCtxIdentifier";
	public static final String BATCH_CTX_TRIP_STATS_EVENT_ID = "eventTripStatsCtxIdentifier";
	public static final String BATCH_CTX_EVENT_ZRAD01_IDS = "eventsZRAD01IdentifierCtx";
	public static final String BATCH_CTX_EVENT_ZRAD02_IDS = "eventsZRAD02IdentifierCtx";
	public static final String BATCH_CTX_VPI_EVENT 			= "eventVpiCtx";
	public static final String BATCH_ROOT_FILE_CTX 			= "rootFileCtx";
	public static final String BATCH_FILE_METADATA_CTX 		= "fileMetadataCtx";
	public static final String BATCH_REGION_PARAM_CTX 		= "regionParamCtx";
	public static final String BATCH_REGION_DATA_CTX 		= "regionDataCtx";
	public static final String BATCH_SECTION_PARAM_CTX 		= "sectionParamCtx";
	public static final String BATCH_SECTION_DATA_CTX 		= "sectionDataCtx";
	public static final String TRIP_SINGLE_MODE = "TRIP_SINGLE_MODE";
	public static final String TRIP_PARTITION_MODE 			= "TRIP_PARTITION_MODE";
	public static final String TRIP_INVALID_MODE 			= "TRIP_INVALID_MODE";
	public static final String TRIP_METRICS_PARTITION_DAILY_MODE = "TRIP_METRICS_PARTITION_DAILY_MODE";
	public static final String TRIP_METRICS_PARTITION_MONTHLY_MODE = "TRIP_METRICS_PARTITION_MONTHLY_MODE";
	public static final String BATCH_ERROR_MESSAGE_CTX = "TRIP_INVALID_MODE";
	public static final String BATCH_TRIP_NO_DATA = "TRIP_NO_DATA";
	public static final String BATCH_TRIP_ON_FAILED = "TRIP_ON_FAILED";

	public static final String PARAM_REGION_CODE = "coRegion";
	public static final String PARAM_SECTION_CODE = "coSection";
	public static final String BATCH_ALL_EVENT_ZRAD02_IDS = "allEventsZRAD02IdentifierCtx";

}
