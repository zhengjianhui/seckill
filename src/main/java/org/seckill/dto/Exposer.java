package org.seckill.dto;


/**
 * 暴露秒杀接口
 * @author zjh
 *
 */
public class Exposer {
	
	/** 秒杀是否开启 */
	private boolean exposed;
	
	/** 加密 */
	private String md5; 
	
	/** 秒杀商品的id */
	private long seckillId;
	
	/** 系统的 当前 时间 */
	private long now; 
	
	
	/** 秒杀开启 时间 */
	private long start; 
	/** 秒杀结束 时间 */
	private long end; 
	
	/**
	 * 
	 * @param exposed       是否开启
	 * @param md5	     	秒杀地址
	 * @param seckillId     秒杀id
	 */
	public Exposer(boolean exposed, String md5, long seckillId) {
		this.exposed = exposed;
		this.md5 = md5;
		this.seckillId = seckillId;
	}
	
	/**
	 * 
	 * @param exposed   是否开启
	 * @param now		当前系统时间
	 * @param start		开启时间
	 * @param end		结束时间
	 */
	public Exposer(boolean exposed,long seckillId, long now, long start, long end) {
		this.exposed = exposed;
		this.now = now;
		this.seckillId = seckillId;
		this.start = start;
		this.end = end;
	}
	
	/**
	 * 
	 * @param exposed		是否开启
	 * @param seckillId		商品id
	 */
	public Exposer(boolean exposed, long seckillId) {
		this.exposed = exposed;
		this.seckillId = seckillId;
	}

	public boolean isExposed() {
		return exposed;
	}

	public void setExposed(boolean exposed) {
		this.exposed = exposed;
	}

	public String getMd5() {
		return md5;
	}

	public void setMd5(String md5) {
		this.md5 = md5;
	}

	public long getSeckillId() {
		return seckillId;
	}

	public void setSeckillId(long seckillId) {
		this.seckillId = seckillId;
	}

	public long getNow() {
		return now;
	}

	public void setNow(long now) {
		this.now = now;
	}

	public long getStart() {
		return start;
	}

	public void setStart(long start) {
		this.start = start;
	}

	public long getEnd() {
		return end;
	}

	public void setEnd(long end) {
		this.end = end;
	}
	
}
