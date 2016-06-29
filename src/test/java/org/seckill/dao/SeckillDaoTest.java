/**
 * 
 */
package org.seckill.dao;


import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.entity.Seckill;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


/**
 * @author zjh
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:config/spring-dao.xml"})
public class SeckillDaoTest {

	@Resource
	private SeckillDao dao;

	/**
	 * Test method for {@link org.seckill.dao.SeckillDao#reduceNumber(long, java.util.Date)}.
	 */
	@Test
	public void testReduceNumber() {
		Date data = new Date();
		int num = dao.reduceNumber(1, data);
		System.out.println(num);
	}

	/**
	 * Test method for {@link org.seckill.dao.SeckillDao#queryByID(long)}.
	 * 
	 */
	@Test
	public void testQueryById() {
		long id = 1;
		Seckill s = dao.queryById(id);
		System.out.println(s.getName());
		
		System.out.println(s.getStartTime());
//		
	}

	/**
	 * Test method for {@link org.seckill.dao.SeckillDao#queryAll(int, int)}.
	 */
	@Test
	public void testQueryAll() {
		List<Seckill> l = dao.queryAll(0, 100);
		for (Seckill seckill : l) {
			System.out.println(seckill.getName());
		}
	}

}
