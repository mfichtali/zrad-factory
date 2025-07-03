package ma.zrad.system.ref.core.infra.persistence.jpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.Accessors;
import ma.zrad.system.batch.common.utils.TableNameUtils;
import ma.zrad.system.ref.core.infra.persistence.jpa.entity.base.AbstractZradEntity;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(name = TableNameUtils.REF_RADAR, schema = TableNameUtils.ZRAD_DEFAULT_SCHEMA)
@NoArgsConstructor
@Accessors(chain = true)
@Getter
@Setter
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@ToString(doNotUseGetters = true, onlyExplicitlyIncluded = true)
@DynamicUpdate
@DynamicInsert
public class RadarRef extends AbstractZradEntity {

    @ToString.Include
    @EqualsAndHashCode.Include
    @Column(name = "CO_RADAR", nullable = false)
    private String codeRadar;

    @ToString.Include
    @EqualsAndHashCode.Include
    @Column(name = "LB_LOCALISATION", nullable = false)
    private String labelLocalisation;

    @Column(name = "IS_ENABLED")
    private boolean enabled = true;

}
