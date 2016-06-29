package org.seckill.dto;

import org.seckill.entity.Successkilled;
import org.seckill.enums.SeckillEnum;

/**
 * 执行秒杀操作的结果
 * @author zjh
 *
 */
public class SeckillExcutetion {

	/** 秒杀商品的id */
	private long seckillId;
	
	/** 秒杀状态 start */
	private int start;
	
	/** 状态的标示 */
	private String stateInfo;
	
	/** 秒杀成功的对象 */
	private Successkilled successkilled;
	
	public SeckillExcutetion(long seckillId, SeckillEnum seckillEnum,
			Successkilled successkilled) {
		super();
		this.seckillId = seckillId;
		this.start = seckillEnum.getStart();
		this.stateInfo = seckillEnum.getStateInif();
		this.successkilled = successkilled;
	}
	
	
	
	public SeckillExcutetion(long seckillId, SeckillEnum seckillEnum) {
		super();
		this.seckillId = seckillId;
		this.start = seckillEnum.getStart();
		this.stateInfo = seckillEnum.getStateInif();
	}






	public long getSeckillId() {
		return seckillId;
	}

	public void setSeckillId(long seckillId) {
		this.seckillId = seckillId;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public String getStateInfo() {
		return stateInfo;
	}

	public void setStateInfo(String stateInif) {
		this.stateInfo = stateInif;
	}

	public Successkilled getSuccesskilled() {
		return successkilled;
	}

	public void setSuccesskilled(Successkilled successkilled) {
		this.successkilled = successkilled;
	}

}
