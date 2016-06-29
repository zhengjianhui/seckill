package org.seckill.service;

import java.util.List;

import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExcutetion;
import org.seckill.entity.Seckill;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SecKillException;
import org.seckill.exception.SeckillECloseException;

/**
 * Seckill 接口 站在使用者的角度设计接口
 * 
 * 方法定义粒度
 * 参数         简练直接
 * 返回类型     （return ， exception）
 * @author zjh
 *
 */
public interface SeckillService {
	
	/**
	 * 获取秒杀产品列表
	 * @return
	 */
	List<Seckill> getSeckillList();
	
	/**
	 * 查询单个秒杀记录
	 * @param SeckillId
	 * @return
	 */
	Seckill getById(long SeckillId);
	
	
	/**
	 * 秒杀开启时 输出秒杀的地址
	 * 否则输出 开启时间 结束时间
	 * @param SeckillId
	 * @return  用于判断秒杀是否开启
	 */
	Exposer exportSeckillUrl(long SeckillId);
	
	
	/**
	 * 执行秒杀操作  如果用户的md5和内部md5 比较改变则拒绝秒杀
	 * @param SeckillId
	 * @param userPhone
	 * @param md5				重开启秒杀时 返回的Exposer 中获取, 绑定用户唯一的秒杀id 在秒杀操作中判断，不符合内部MD5 规则则认定失败
	 * @return SeckillExcutetion  
	 * @exception SecKillException		
	 * @exception SeckillECloseException 
	 * @exception RepeatKillException
	 */
	SeckillExcutetion executeSeckill(long SeckillId, long userPhone, String md5) 
			throws SecKillException, SeckillECloseException, RepeatKillException;
}
