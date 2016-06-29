package org.seckill.exception;

/**
 * 秒杀关闭 异常
 * @author zjh
 *
 */
@SuppressWarnings("serial")
public class SeckillECloseException extends SecKillException {

	public SeckillECloseException(String message, Throwable cause) {
		super(message, cause);
	}

	public SeckillECloseException(String message) {
		super(message);
	}

	
	
}
