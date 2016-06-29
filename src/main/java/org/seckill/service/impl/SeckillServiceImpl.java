package org.seckill.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.MapUtils;
import org.seckill.dao.SeckillDao;
import org.seckill.dao.SuccesskilledDao;
import org.seckill.dao.cache.RedisDao;
import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExcutetion;
import org.seckill.entity.Seckill;
import org.seckill.entity.Successkilled;
import org.seckill.enums.SeckillEnum;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SecKillException;
import org.seckill.exception.SeckillECloseException;
import org.seckill.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

/**
 * SeckillService 接口实现
 * @author zjh
 * @param <thorw>
 *
 */
@Service
public class SeckillServiceImpl implements SeckillService {
	
	/** 统一日志管理 */
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Resource
	private SeckillDao seckillDao;

	@Resource
	private SuccesskilledDao successkilledDao;
	
	@Resource
	private RedisDao redisDao;
	
	
	/** 用于混淆MD5  假地址，防止用户猜出地址 */
	private final String slat = "wijoiajdsio!@#$sduhiauOIJOQIWJ"; 
	

	@Override
	public List<Seckill> getSeckillList() {
		return seckillDao.queryAll(0, 4);
	}

	@Override
	public Seckill getById(long seckillId) {
		return seckillDao.queryById(seckillId) ;
	}

	@Override
	public Exposer exportSeckillUrl(long seckillId) {
		// 获取秒杀详情  使用 redis 缓存起来  降低数据库压力
		// redisDao 缓存优化
		// 访问 redis
		Seckill seckill = redisDao.getSeckill(seckillId);
		// 判断秒杀对象是否存在
		if(seckill == null) {
			// 访问数据库
			seckill = seckillDao.queryById(seckillId);
			
			if(seckill == null) {
				return new Exposer(false, seckillId);
			} else {
				// 如果数据库有这条数据则放入缓存
				redisDao.putSeckill(seckill);
			}
			
		}
		
		
		
//		Seckill seckill = seckillDao.queryById(seckillId);
		
		// 判断秒杀对象是否存在
//		if(seckill == null) {
//			return new Exposer(false, seckillId);
//		}
		// 获取开启时间 结束时间 系统当前时间
//		Date start = seckill.getStartTime();
//		Date end = seckill.getEndTime();
		Date now = new Date();
		// 判断秒杀是否开启
		if(now.getTime() < seckill.getStartTime().getTime() || now.getTime() > seckill.getEndTime().getTime()) {
			
			return new Exposer(false, seckillId, now.getTime(), seckill.getStartTime().getTime(), seckill.getEndTime().getTime());
		}
		
		// 转化特定字符串  用商品的id  和 md5加密的id做比较 防止用户提前知道针对接口的从URL注入篡改 修改商品id
		// 用户传输的 MD5 和 内部生产的MD5 进行比较 如果该了 id  或  md5 则匹配不上认定失败
		// 秒杀接口 执行该 判断 executeSeckill
		String md5 = md5(seckillId);
		return new Exposer(true, md5, seckillId);
	}
	
	/**
	 * 和 slat 组合 成md5
	 * @param seckillId
	 * @return
	 */
	private String md5(long seckillId) {
		String base = seckillId + "/" + slat;
		String md5 = DigestUtils.md5DigestAsHex(base.getBytes());
		
		return md5;
	}

	
	/**
	 * 1.开发团队达成一致的约定，明确标注事物注解的编程风格
	 * 2.保证事物方法的执行 尽可能短, 不要穿插其他网络操作  RPC（缓存）/HTTP请求 如果必须要 就 剥离出来单独的方法
	 * 3.不是所有方法都需要事物不需要并发操作，例如单条的 CRUD   
	 * 
	 * 4.由于 update 时 多条线程都在插入同一行事物， 而这时数据库的这条锁是被 行级锁锁住的，所以先执行插入语句
	 * 	 再执行 update （在insert 成功 的情况下） 可以减少 线程持有行级锁的时间提升性能
	 */
	@Override
	@Transactional
	public SeckillExcutetion executeSeckill(long seckillId, long userPhone,
			String md5) throws SecKillException, SeckillECloseException,
			RepeatKillException {
		
		try{
			// 比较用户的md5  和 id 是否修改  md5.equals(md5(seckillId))
			if(md5 == null || !md5.equals(md5(seckillId))) { 
				
				// 如果 MD5  比较失败 则抛出异常 提示重写秒杀 订单
				throw new SecKillException("seckill data rewrite");
				
			} else {
				// 先执行插入行为  
				// seckillId, userPhone 联合主键 如果用户的 userPhone 电话号码在表中已经存在 则对应id的秒杀产品插入失败 返回0条记录
				int insertNum = successkilledDao.insertSuccessKilled(seckillId, userPhone);
				if(insertNum <= 0) {
					// 抛出异常 提示用户重复秒杀
					throw new RepeatKillException("seckill repeated");
				} else {
					// 插入记录大于 0 时 
					// 执行秒杀逻辑 减库存
					// killtime  秒杀的时间 sql中会判定是否在秒杀时间 内 库存是否还有 num 返回值为update后的结果
					Date killtime = new Date();
					int num = seckillDao.reduceNumber(seckillId, killtime);
					// 判断是否减少了库存
					if(num <= 0) {
						// 抛出异常 提示用户秒杀结束  抛出异常执行 rollBack
						throw new SeckillECloseException("seckill data close");
					} else {	
						// 判定秒杀成功 执行 commit
						Successkilled s = successkilledDao.queryByIdWithSeckill(seckillId, userPhone);
						return new SeckillExcutetion(seckillId,SeckillEnum.SUCCESS, s);
					}
			}

			} 

		} catch(SeckillECloseException e2) {
			throw e2;
		} catch(RepeatKillException e3) {
			throw e3;
		} catch(Exception e) {
			// 记录日志 
			logger.error(e.getMessage(), e);
			
			// 将所有异常转为运行时异常 抛出 统一异常方便事物回滚
			throw new SecKillException("error : " + e.getMessage());
		}
		
	}

	
	
	
	
	
	@Override
	public SeckillExcutetion executeSeckillProcedure(long seckillId,
			long userPhone, String md5) {
		if(md5 == null || !md5.equals(md5(seckillId))) {
			// 返回一个数据被篡改的结果
			return new SeckillExcutetion(seckillId, SeckillEnum.DATE_REWRITE);
		}
		
		// 获取当前系统时间
		Date date = new Date();
		Map<String,Object> map = new HashMap<String, Object>();
		
		map.put("seckillId", seckillId);
		map.put("phone", userPhone);
		map.put("killTime", date);
		map.put("result", null);
		// 存储过程玩 result 被赋值
		
		try {
			seckillDao.killByProcedure(map);
			// 获取 result 获取map中的 result 如果没有则默认 -2
			int result = MapUtils.getInteger(map, "result", -2);
			
			// 判断结果
			if(result == 1) {
				Successkilled sk = successkilledDao.queryByIdWithSeckill(seckillId, userPhone);
				
				return new SeckillExcutetion(seckillId,SeckillEnum.SUCCESS, sk);
			} else {
				return new SeckillExcutetion(seckillId,SeckillEnum.stateOf(result));
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return new SeckillExcutetion(seckillId,SeckillEnum.INNER_ERROR);
		}
		
	}

}
