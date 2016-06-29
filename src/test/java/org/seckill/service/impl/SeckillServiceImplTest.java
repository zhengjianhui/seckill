package org.seckill.service.impl;

import static org.junit.Assert.*;

import java.util.List;

import javax.annotation.Resource;

import org.junit.AfterClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExcutetion;
import org.seckill.entity.Seckill;
import org.seckill.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({
		"classpath:config/spring-dao.xml",
		"classpath:config/spring-service.xml"
})
public class SeckillServiceImplTest {
	
	@Resource
	private SeckillService seckillServiceImpl;
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());


	@Test
	public void testGetSeckillList() {
		List<Seckill> list = seckillServiceImpl.getSeckillList();
		logger.info("list={}", list);
	}

	@Test
	public void testGetById() {
		long id = 1L;
		Seckill s = seckillServiceImpl.getById(id);
		logger.info("s={}",s);  // s=org.seckill.entity.Seckill@49c66ade
	}	

	@Test
	public void testExportSeckillUrl() {
		long id = 1L;
		Exposer e = seckillServiceImpl.exportSeckillUrl(id);
		logger.info("e={}",e);  
		System.out.println(e.getMd5());   // c8c968297ba1666ee63743d96469937d
	}

	@Test
	public void testExecuteSeckill() {
		long id = 1L;
		long phone = 133333333333l;
		String md5 = "c8c968297ba1666ee63743d96469937d";
		try{
			SeckillExcutetion ss = seckillServiceImpl.executeSeckill(id, phone, md5);
		
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

	}

}
