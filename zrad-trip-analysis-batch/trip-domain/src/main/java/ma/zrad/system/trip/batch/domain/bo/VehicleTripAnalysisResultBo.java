package ma.zrad.system.trip.batch.domain.bo;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import ma.zrad.system.batch.common.enums.CodeImportStatusEnum;
import ma.zrad.system.batch.common.records.VehiclePassageSimpleAggregateRecord;
import ma.zrad.system.trip.batch.domain.model.VehicleTripDomain;

import java.io.Serializable;

@Accessors(chain = true)
@NoArgsConstructor
@Getter
@Setter
public class VehicleTripAnalysisResultBo implements Serializable {

    @Setter(AccessLevel.NONE)
    private VehiclePassageSimpleAggregateRecord tripAggregate;
    private VehicleTripDomain tripDomain;
    private String rnVehicle;
    private CodeImportStatusEnum status;

    public VehicleTripAnalysisResultBo setTripAggregate(final VehiclePassageSimpleAggregateRecord tripAggregate) {
        this.tripAggregate = tripAggregate;
        if(tripAggregate != null && tripAggregate.rnVehicle() != null) {
            this.setRnVehicle(tripAggregate.rnVehicle());
        }
        return this;
    }
}
