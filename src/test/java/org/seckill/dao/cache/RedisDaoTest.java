package org.seckill.dao.cache;


import javax.annotation.Resource;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.dao.SeckillDao;
import org.seckill.entity.Seckill;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:config/spring-dao.xml"})
public class RedisDaoTest {

	@Resource
	private RedisDao rDao;
	
	
	@Resource
	private SeckillDao dao;

	@Test
	public void testRedisDao() {
		// 尝试从 redisPool 中获取 缓冲的对象
		Seckill seckill = rDao.getSeckill(1);
		
		// 如果没有对象
		if(seckill == null) {
			// 尝试去数据库取出对象
			seckill = dao.queryById(1);
			
			// 如果数据库有对象
			if(seckill != null) {
				// 将对象添加到缓冲区
				String result = rDao.putSeckill(seckill);
				System.out.println(result);
				
				// 从缓冲区得到对象
				seckill = rDao.getSeckill(1);
				System.out.println(seckill.getName());
			}
			
		}
	
	}


}
