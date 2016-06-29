package org.seckill.controller;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExcutetion;
import org.seckill.dto.SeckillResult;
import org.seckill.entity.Seckill;
import org.seckill.enums.SeckillEnum;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillECloseException;
import org.seckill.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * 路由控制
 * 
 * @author zjh
 *
 */
@Controller
@RequestMapping("/seckill")
// 模块 url:/模块/资源/{参数}/细分 /seckill/list
public class SeckillController {

	// 日志对象
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Resource
	private SeckillService seckillServiceImpl;

	/**
	 * 获取秒杀 列表
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(Model model) {
		// 获取列表页
		List<Seckill> list = seckillServiceImpl.getSeckillList();
		model.addAttribute("list",list);

		return "list";
	}

	/**
	 * 获取秒杀详情
	 * 
	 * @param seckillId
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "{seckillId}/detail", method = RequestMethod.GET)
	public String detail(@PathVariable("seckillId") Long seckillId, Model model) {
		// 如果请求不存在重定向到 列表页
		if (seckillId == null) {
			return "redirect:/seckill/list";
		}

		Seckill seckill = seckillServiceImpl.getById(seckillId);
		// 不存在秒杀对象时 转发到列表页
		if (seckill == null) {
			return "forward/seckill/list";
		}
		

		// 不为空时 将值传递给页面
		model.addAttribute("seckill", seckill);

		return "detail";
	}

	/**
	 * 查看秒杀开启是否
	 * 
	 * @param seckillId
	 * @return
	 */
	@RequestMapping(value = "{seckillId}/exposer", method = RequestMethod.POST, produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	public SeckillResult exposer(@PathVariable("seckillId") Long seckillId) {
		SeckillResult<Exposer> result;

		try {
			Exposer e = seckillServiceImpl.exportSeckillUrl(seckillId);
			result = new SeckillResult<Exposer>(true, e);
			// 若秒杀没有开启则抛出异常
		} catch (Exception e) {
			logger.error(e.getMessage());
			// 传递状态为 false 和 异常名
			result = new SeckillResult<Exposer>(false, e.getMessage());
		}

		return result;
	}

	/**
	 * 执行秒杀逻辑
	 * @param seckillId
	 * @param userPhone
	 * @param md5
	 * @return
	 */
	@RequestMapping(value = "{seckillId}/{md5}/execution", method = RequestMethod.POST, produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	public SeckillResult<SeckillExcutetion> execute(
			@PathVariable("seckillId") Long seckillId,
			@CookieValue(value = "userPhone", required = false) Long userPhone,
			@PathVariable("md5") String md5) {
		
		// 验证信息
		if(userPhone == null) {
			return new SeckillResult<SeckillExcutetion>(false, "未注册");
		}
		
		SeckillResult<SeckillExcutetion> result;
		SeckillExcutetion seckillExecution;
		
		try {
			
			SeckillExcutetion se = seckillServiceImpl.executeSeckill(seckillId, userPhone, md5);
			result = new SeckillResult<SeckillExcutetion>(true, se);
			
			
		} catch(SeckillECloseException e2) {
			seckillExecution = new SeckillExcutetion(seckillId, SeckillEnum.END);
            result = new SeckillResult<SeckillExcutetion>(true,seckillExecution);
		} catch(RepeatKillException e3) {
			seckillExecution = new SeckillExcutetion(seckillId, SeckillEnum.REPEAT_KILL);
            result = new SeckillResult<SeckillExcutetion>(true,seckillExecution);
		} catch (Exception e) {
			seckillExecution = new SeckillExcutetion(seckillId, SeckillEnum.INNER_ERROR);
            result = new SeckillResult<SeckillExcutetion>(true,seckillExecution);
		}
		
		return result;

	}
	
	/**
	 * 获取系统时间
	 * @param model
	 * @return
	 */
    @RequestMapping(value = "time/now",method = RequestMethod.GET,produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public SeckillResult<Long> execute(Model model) {
        return new SeckillResult<Long>(true,new Date().getTime());
    }
}
