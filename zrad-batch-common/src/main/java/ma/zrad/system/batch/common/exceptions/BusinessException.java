package ma.zrad.system.batch.common.exceptions;

import lombok.Getter;
import ma.zrad.system.batch.common.enums.BatchErrorTypeEnum;

@Getter
public class BusinessException extends RuntimeException {

	private final BatchErrorTypeEnum errorType;

	public BusinessException(String msg) {
		super(msg);
		this.errorType = BatchErrorTypeEnum.FUNCTIONAL;
	}
	
	public BusinessException(String msg, Throwable cause) {
		super(msg, cause);
		this.errorType = BatchErrorTypeEnum.FUNCTIONAL;
	}
}
