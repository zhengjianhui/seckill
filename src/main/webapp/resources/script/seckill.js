// 存放主要交互逻辑代码
// js 模块化 闭包

var seckill = {
	// 封装秒杀相关地址
	URL:{
		now:function() {
			return '/seckill/seckill/time/now';
		},
        exposer: function (seckillId) {
            return '/seckill/seckill/' + seckillId + '/exposer';
        },
		execution: function (seckillId, md5) {
            return '/seckill/seckill/' + seckillId + '/' + md5 + '/execution';
        }
	},	
	
	// 验证手机号
	validatePhone:function(phone) {
		// 验证手机号 是不是null  长度是不是11 !isNaN 是不是数字
		if(phone && phone.length == 11 && !isNaN(phone)) {
			return true;
		} else {
			return false;
		}
	},
	
	// 时间判断
	countdown:function(seckillId, nowTime, startTime, endTime) {
		
		// 获取显示时间的节点 seckill-box
		var seckillBox = $('#seckill-box');
		
		// 判断秒杀是否结束
		if(nowTime > endTime) {
			seckillBox.html('秒杀结束!');
			// 判断秒杀是否开启
		} else if(nowTime < startTime) {
			// 使用js获取 startTime 的时间
			var killTime = new Date(startTime + 1000);
			
			// 计时 jquery 提供的计时插件 第一个参数为时间  第二个参数为回调函数
			seckillBox.countdown(killTime, function(event) {
				
				// 时间格式
				var format = event.strftime('秒杀倒计时: %D天 %H时 %M分 %S秒 ');
				// 输出时间
				seckillBox.html(format);
				
				/** 绑定时间完成后回调事件 */
			}).on('finish.countdown', function() {
				// 获取秒杀地址 控制秒杀逻辑
				 seckill.handlerSeckill(seckillId,seckillBox)
			});
		} else {
			// 秒杀开启逻辑
			 seckill.handlerSeckill(seckillId,seckillBox);
		}
	},
	
	
	// 获取秒杀地址 执行秒杀逻辑
	handlerSeckill: function (seckillId, seckillBox) {
		
		
		// 控制节点  将时间显示节点替换成 提交按钮
		seckillBox.hide().html('<button class="btn btn-primary btn-lg" id="killBtn">开始秒杀</button>');
		
		// 拿到地址  和 MD5
		$.post(seckill.URL.exposer(seckillId),{},function(result){
			// 在回调函数中执行交互流程
			
			if(result && result['success']) {
				var exposer = result['data']
				
				// 查看是否开启秒杀
				if(exposer['exposed']) {
					// 开启秒杀获取 md5
					var md5 = exposer['md5']
					var killurl = seckill.URL.execution(seckillId, md5)
					
					// 绑定秒杀 按钮事件  使用one 绑定一次点击事件 防止重复点击 对服务器造成压力
					$('#killBtn').one('click',function() {
						// 点击后触发事件 禁用按钮
						$(this).addClass('disabled')
						
						// 发送post 请求 执行秒杀逻辑
						$.post(killurl,{},function(result) {
							
							if(result && result['success']) {
								// 获取秒杀 结果
								var killResult = result['data'];
								// 状态
                                var state = killResult['state'];
                                // 描述
                                var stateInfo = killResult['stateInfo'];
                                // 显示秒杀结果
                                seckillBox.html('<span class="label label-success">' + stateInfo + '</span>');
							}
						})
						
					});
					// 显示结果
					seckillBox.show();
				} else {
					// 未开启秒杀  解决用户 pc 端和 服务器时间不同步
					var now = exposer['now']
					var start = exposer['start']
					var end = exposer['end']
					
					// 继续等待秒杀流程  重新计算计时逻辑
					seckill.countdown(seckillId, now, start, end) 
				}
				
			} else {
				console.log(result)
			}
		})
		
	},
	
	
	// 详情页详情秒杀逻辑
	detail:{
		// 详情页初始化
		init:function(params) {
			// 用户的手机验证 与 计时操作
			// 规划交互流程
			// 从cookie 中获取手机号
			var userPhone = $.cookie('userPhone');
			var startTime = params['startTime'];
			var endTime = params['endTime'];
			var seckillId = params['seckillId'];
			
		
			// 不服条件则 提示
			if(!seckill.validatePhone(userPhone)) {
				// 绑定手机号
				var killPhoneModal = $('#killPhoneModal');
				// Boot 自带的组件
				killPhoneModal.modal({
					show:true, // 显示弹出层
					backdrop:'static',  // 禁止关闭
					keyboard:false // 关闭键盘事件
				});
				
				// 事件绑定
				$('#killPhoneBtn').click(function() {
					// 获取 input 的值
					var inputPhone = $('#killPhoneKey').val();
					
					if(seckill.validatePhone(inputPhone)) {
						
						// 写入cookie {expires:7, path:'/seckill'} 保留七天 只在 seckill模块下有效
						$.cookie('userPhone', inputPhone, {expires:7, path:'/seckill'});
						// 刷新页面
						window.location.reload();
					} else {
						// 隐藏页面并清空 页面 后 动画显示
						$('#killPhoneMessage').hide().html('<label class="label label-danger">手机号错误!</label>').show(500);
					}
				})
			}
			// 验证过后说明已经登入
			// 处理记时操作
			$.get(seckill.URL.now(),function(result) {
				// 判断状态是否为 true
				if(result && result['success']) {
					var nowTime = result['data'];
					// 时间判断
					seckill.countdown(seckillId, nowTime, startTime, endTime) ;
						
					} else {
						console.log(result);
					}
				
				});
		}
		
	
	},
		
		
}
