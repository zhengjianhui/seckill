package org.seckill.exception;


/**
 * 重复秒杀异常
 * @author zjh
 *
 */
@SuppressWarnings("serial")
public class RepeatKillException extends SecKillException {

	public RepeatKillException(String message, Throwable cause) {
		super(message, cause);
	}

	public RepeatKillException(String message) {
		super(message);
	}

}
