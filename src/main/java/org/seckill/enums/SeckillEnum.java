package org.seckill.enums;


/**
 * 枚举包，用于给前段判断 状态
 * enum 类型 迭代所有
 * @author zjh
 *
 */
public enum SeckillEnum {
	SUCCESS (1, "秒杀成功"),
	END(0, "秒杀失败"),
	REPEAT_KILL(-1, "重复秒杀"),
	INNER_ERROR(-2, "系统异常"),
	DATE_REWRITE(-3, "数据篡改");

	/** 秒杀状态 start */
	private int start;
	
	/** 状态的标示 */
	private String stateInif;

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public String getStateInif() {
		return stateInif;
	}

	public void setStateInif(String stateInif) {
		this.stateInif = stateInif;
	}

	SeckillEnum(int start, String stateInif) {
		this.start = start;
		this.stateInif = stateInif;
	}
	
	/**
	 * 根据state 字段迭代
	 * @param index
	 * @return
	 */
	public static SeckillEnum stateOf(int index) {
		for (SeckillEnum state : values()) {
			
			if(state.getStart() == index) {
				return state;
			}
		}
		
		return null;
	}
	
	
}
