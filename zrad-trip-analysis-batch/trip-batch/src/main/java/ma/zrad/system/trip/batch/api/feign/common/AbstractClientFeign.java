package ma.zrad.system.trip.batch.api.feign.common;

public interface AbstractClientFeign {

    String REF_CORE_SERVICE_NAME = "${application.feign.client.b2b.ref-core.service-name:}";
    String REF_CORE_SERVICE_URL = "${application.feign.client.b2b.ref-core.service-url:}";

}
