package ma.zrad.system.ref.core.infra.rest;

import lombok.RequiredArgsConstructor;
import ma.zrad.system.batch.common.pojo.FullRegionDetailsOut;
import ma.zrad.system.ref.core.domain.port.in.RegionReferentialService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ref")
@RequiredArgsConstructor
public class ApiReferentialCoreResource {

    private final RegionReferentialService regionReferentialService;

    @GetMapping("/regions/details")
    @ResponseStatus(HttpStatus.OK)
    public List<FullRegionDetailsOut> getRegionDetails(
            @RequestParam(value = "code", required = false) String regionCode) {
        if (isCodeProvided(regionCode)) {
            return regionReferentialService.getRegionWithRadarSections(sanitizeCode(regionCode));
        }
        return regionReferentialService.getAllRegionsWithRadarSections();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleBadRequest(IllegalArgumentException e) {
        return ResponseEntity.badRequest()
                .body(Map.of("error", "Invalid parameter", "message", e.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGenericError(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Internal server error", "message", "An error occurred"));
    }

    private boolean isCodeProvided(String code) {
        return code != null && !code.isBlank();
    }

    private String sanitizeCode(String code) {
        return code.trim().toUpperCase();
    }
}
