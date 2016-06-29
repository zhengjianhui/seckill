package org.seckill.exception;


/**
 * 秒杀相关异常
 * @author zjh
 *
 */
@SuppressWarnings("serial")
public class SecKillException extends RuntimeException{

	public SecKillException(String message, Throwable cause) {
		super(message, cause);
	}

	public SecKillException(String message) {
		super(message);
	}

}
