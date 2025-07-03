package ma.zrad.system.stats.batch.infra.config;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ZradScanPackages {

    public static final String BATCH_BASE_PACK = "ma.zrad.system.stats.batch";
    public static final String BATCH_COMMON_PACK = "ma.zrad.system.batch.common";
    public static final String BATCH_COMMON_PERSISTENCE_ENTITY_SCAN = BATCH_COMMON_PACK + ".persistence.jpa.entity";

    // === Batch ===
    public static final String BATCH_REST = BATCH_BASE_PACK + ".rest";
    public static final String BATCH_LISTENER = BATCH_BASE_PACK + ".listener";
    public static final String BATCH_TASKLET = BATCH_BASE_PACK + ".tasklet";
    public static final String BATCH_READER = BATCH_BASE_PACK + ".reader";
    public static final String BATCH_SERVICE = BATCH_BASE_PACK + ".service";
    public static final String BATCH_API_FEIGN = BATCH_BASE_PACK + ".api.feign";

    // === Infrastructure ===
    public static final String INFRA_PACK = BATCH_BASE_PACK + ".infra";
    public static final String INFRA_REPOSITORY = INFRA_PACK + ".persistence.jpa.repository";
    public static final String INFRA_ADAPTER = INFRA_PACK + ".persistence.jpa.adapter";
    public static final String INFRA_MAPPER = INFRA_PACK + ".mapper";

    // === Business ===
    public static final String BUSINESS_PACK = BATCH_BASE_PACK + ".business";
    public static final String BUSINESS_SERVICE = BUSINESS_PACK + ".service";
    public static final String BUSINESS_PORT_OUT = BUSINESS_PACK + ".port.out";

    // === Domain ===
    public static final String DOMAIN_PACK = BATCH_BASE_PACK + ".domain";
    public static final String DOMAIN_PORT_IN = DOMAIN_PACK + ".port.in";
    public static final String DOMAIN_PORT_OUT = DOMAIN_PACK + ".port.out";

}
