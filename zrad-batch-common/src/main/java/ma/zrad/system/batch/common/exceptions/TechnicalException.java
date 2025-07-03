package ma.zrad.system.batch.common.exceptions;

import lombok.Getter;
import ma.zrad.system.batch.common.enums.BatchErrorTypeEnum;

@Getter
public class TechnicalException extends RuntimeException{

	private final BatchErrorTypeEnum errorType;

	public TechnicalException(String msg) {
		super(msg);
		this.errorType = BatchErrorTypeEnum.TECHNICAL;
	}
	
	public TechnicalException(String msg, Throwable cause) {
		super(msg, cause);
		this.errorType = BatchErrorTypeEnum.TECHNICAL;
	}
    
}
