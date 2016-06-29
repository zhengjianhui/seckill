package org.seckill.dao;

import static org.junit.Assert.*;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.entity.Successkilled;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:config/spring-dao.xml"})
public class SuccesskilledDaoTest {

	
	@Resource
	private SuccesskilledDao dao;
	
	
	@Test
	public void testInsertSuccesskilled() {
		dao.insertSuccessKilled(1, 18659435609L);
	}

	@Test
	public void testQueryById() {
		Successkilled s = dao.queryByIdWithSeckill(1, 18659435609L);
		System.out.println(s.getUserPhone());
	}

}
