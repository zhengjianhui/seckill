package org.seckill.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.seckill.entity.Seckill;

public interface SeckillDao {

	
	/**
	 * 
	 * @param seckillId  减库存的 id
	 * @param killtime	 减库存的 时间
	 * @return  如果影响行数 > 1 表示更新成功的行数
	 */
	int reduceNumber(@Param("seckillId")long seckillId, @Param("killTime")Date killTime);
	
	
	/**
	 * 根据ID查找秒杀库存对象
	 * @param seckillId
	 * @return
	 */
	Seckill queryById(long seckillId);
	
	
	/**
	 * 根据偏移量 查询秒杀的商品有哪些
	 * @param offet   偏移量
	 * @param limit	  查询条数
	 * @return
	 * 
	 * @Param("offet")  java 在运行期是不会 保存形参  而是以 arg0  arg1的形式发送参数
	 * 这时候在 mybatis 中传入两个参数  则会发生异常 告知找不到对应参数 名
	 * queryAll(int offset, int limit);  运行时 以queryAll(int arg0, int arg1);
	 * 可以通过 @Param("offet") 解决这个问题
	 * org.mybatis.spring.MyBatisSystemException: nested exception is 
	 * org.apache.ibatis.binding.BindingException: Parameter 'offset' not found. Available parameters are [offet, limit, param1, param2]
	 */
	List<Seckill> queryAll(@Param("offset") int offset, @Param("limit") int limit);
	
	/**
	 * 使用存储过程 执行秒杀
	 * @param paramsMap
	 */
	void killByProcedure(Map<String, Object> paramsMap);
}
