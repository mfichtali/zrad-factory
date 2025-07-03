package ma.zrad.system.ref.core.infra.config;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ZradRefCoreScanPackages {

    // === Infrastructure ===
    public static final String INFRA_PACK = "ma.zrad.system.ref.core.infra";
    public static final String INFRA_REPOSITORY = INFRA_PACK + ".persistence.jpa.repository";
    public static final String INFRA_ENTITY = INFRA_PACK + ".persistence.jpa.entity";
    public static final String INFRA_ADAPTER = INFRA_PACK + ".persistence.jpa.adapter";
    public static final String INFRA_MAPPER = INFRA_PACK + ".mapper";
    public static final String INFRA_REST = INFRA_PACK + ".rest";

    // === Business ===
    public static final String BUSINESS_PACK = "ma.zrad.system.ref.core.business";
    public static final String BUSINESS_SERVICE = BUSINESS_PACK + ".service";
    public static final String BUSINESS_PORT_OUT = BUSINESS_PACK + ".port.out";

    // === Domain ===
    public static final String DOMAIN_PACK = "ma.zrad.system.ref.core.domain";
    public static final String DOMAIN_PORT_IN = DOMAIN_PACK + ".port.in";
    public static final String DOMAIN_PORT_OUT = DOMAIN_PACK + ".port.out";

}
