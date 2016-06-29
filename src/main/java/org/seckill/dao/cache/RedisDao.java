package org.seckill.dao.cache;

import org.seckill.entity.Seckill;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.runtime.RuntimeSchema;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * 处理redis缓存
 * @author zjh
 *
 */
public class RedisDao {
	
	// 日志对象
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	/** Jedis 的jedis池对象 保存 jedis对象 */
	private final JedisPool jedisPool;
	
	// schema 模式
	/** 自定义序列化  泛型为要转化的对象的class   class 要满足 pojo对象（有get/set）
	 * 	RuntimeSchema.createFrom(Seckill.class)   Seckill.class 传入对象的字节码属性
	 *  将获取的 序列化后的 二进制数据 对应给定的Class  中的属性赋值
	 */
	private RuntimeSchema<Seckill> schema = RuntimeSchema.createFrom(Seckill.class);
	
	/**
	 * 
	 * @param ip		ip地址
	 * @param port		端口号
	 */
	public RedisDao(String ip, int port) {
		jedisPool = new JedisPool(ip, port);
	}
	
	
	/**
	 * 从缓存中 获取 Seckill 对象
	 * 使用protostuff 辅助序列化
	 * @param seckillId
	 * @return
	 */
	public Seckill getSeckill(long seckillId) {
		// redis 操作
		try {
			// 获取Jedis 对象  从jedisPool 池中获取
			Jedis jedis = jedisPool.getResource();
			try {
				// key 值定义
				String key = "seckill:" + seckillId;
				
				// Redis 并没有实现序列化
				// get -> byte[]  获取回来的对象是个 二进制数组  -> 反序列化  -> Object(Seckill)
				// 不使用java 自带的 序列化实现接口  采用自定的序列化组件
				
				// 将key 转化成二进制的 byte数组 输入get 方法  返回对应key 的 value 二进制数组
				byte[] bytes = jedis.get(key.getBytes());
				
				// 判断字节数组是否为空，为空时则缓冲区没有改对象的序列化
				if(bytes != null) {
					
					// 使用 protostuff 创建一个空对象
					Seckill message = schema.newMessage();
					// protostuff 提供的工具类ProtostuffIOUtil 用于给空对象 赋值（将二进制中的对应值赋给空对象）
					ProtostuffIOUtil.mergeFrom(bytes, message, schema);
					
					
					return message;
				}
				
			} finally {
				// 关闭池
				jedis.close();
			}
			
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		
		
		return null;
	}
	
	/**
	 * 在缓存中增加 Seckill 对象
	 * 使用protostuff 辅助序列化
	 * @param seckill
	 * @return
	 */
	public String putSeckill(Seckill seckill) {
		// get 一个 Seckill -> byte[]  转换字节数组 
		try {
			Jedis jedis = jedisPool.getResource();
			
			try {
				
				String key = "seckill:" + seckill.getSeckillId();
				// 将对象序列化 
				// LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE) 缓存器 LinkedBuffer.DEFAULT_BUFFER_SIZE 默认大小
				byte[] bytes = ProtostuffIOUtil.toByteArray(seckill, schema, LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE));
				
				// 超时缓存  timeOut为超时时间  单位 秒/s
				int timeOut = 60 * 60;
				// 返回一个 String  存入则返回 OK 反则返回错误信息
				String result = jedis.setex(key.getBytes(), timeOut, bytes);
				
				return result;
			} finally {
				// 关闭连接
				jedis.close();
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		
		
		return null;
	}
	
	
	
	
}
