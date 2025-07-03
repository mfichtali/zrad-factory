package ma.zrad.system.ref.core.infra.persistence.jpa.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;
import ma.zrad.system.batch.common.utils.TableNameUtils;
import ma.zrad.system.ref.core.infra.persistence.jpa.entity.base.AbstractZradEntity;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = TableNameUtils.REF_REGION, schema = TableNameUtils.ZRAD_DEFAULT_SCHEMA)
@NoArgsConstructor
@Accessors(chain = true)
@Getter
@Setter
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@ToString(doNotUseGetters = true, onlyExplicitlyIncluded = true)
@DynamicUpdate
@DynamicInsert
public class RegionRef extends AbstractZradEntity {

    @ToString.Include
    @EqualsAndHashCode.Include
    @Column(name = "CO_REGION", nullable = false)
    private String codeRegion;

    @ToString.Include
    @EqualsAndHashCode.Include
    @Column(name = "LB_REGION", nullable = false)
    private String labelRegion;

    @JsonManagedReference
    @OneToMany(mappedBy = "region", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<SectionRef> sections = new HashSet<>(0);

}
