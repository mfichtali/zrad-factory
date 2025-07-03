package ma.zrad.system.batch.common.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@SuperBuilder
@JsonInclude(Include.NON_NULL)
public abstract class BaseWrapper implements Serializable{
	protected boolean success;
	protected String date;
	protected String message;
}
