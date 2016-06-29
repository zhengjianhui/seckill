package org.seckill.dao;

import org.apache.ibatis.annotations.Param;
import org.seckill.entity.Successkilled;

/**
 * 秒杀单
 * @author zjh
 *
 */
public interface SuccesskilledDao {

	/**
	 * 
	 * 插入购买 明细  可以过滤重复
	 * （联合唯一 主键）
	 * 两个参数  第一个为ID  第二个为电话
	 * @param seckilledId
	 * @param userphone
	 * @return  插入的结果和 行数
	 */
	int insertSuccessKilled(@Param("seckillId")long seckillId, @Param("userPhone")long userPhone);
	
	
	/**
	 * 根据ID 去查询秒杀商品  并携带秒杀产品对象
	 * @param seckilledId
	 * @return
	 */
	Successkilled queryByIdWithSeckill(@Param("seckillId")long seckillId, @Param("userPhone")long userPhone);
}
