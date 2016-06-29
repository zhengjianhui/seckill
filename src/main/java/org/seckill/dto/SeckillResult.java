package org.seckill.dto;

/**
 * 用来封装JSON
 * @author zjh
 *
 * @param <T>
 */
public class SeckillResult<T> {

	/** 判断状态, true 输出 data false 输出error */
	private boolean success;
	
	private T data;
	
	private String error;

	public SeckillResult(boolean success, String error) {
		super();
		this.success = success;
		this.error = error;
	}

	public SeckillResult(boolean success, T data) {
		super();
		this.success = success;
		this.data = data;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}
	
	
}
