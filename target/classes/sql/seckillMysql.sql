-- 存储过程

DELIMITER $$   -- 修改；号换行， 装换为 $$


-- 定义存储过程
-- 参数的定义 
-- in  输入的参数
-- out 输出的参数
-- row_count();  返回上一条修改类型sql(delete,insert,update) 的印象函数
-- row_count()   结果 0 则没有修改成功  成功则返回修改条数  < 0 则为修改

-- CREATE PROCEDURE 创建一个储存过程
-- BEGIN 开始存储过程的定义
-- DECLARE insert_count INT DEFAULT 0;  声明一个变量  int 型 默认为0
-- START TRANSACTION; 开启事物
-- SELECT ROW_COUNT() INTO insert_count; 将查询结果赋值


CREATE PROCEDURE seckill.execute_seckill
  (IN v_seckill_id BIGINT, IN v_phone BIGINT,
   IN v_kill_time  TIMESTAMP, OUT r_result INT)
  BEGIN
    DECLARE insert_count INT DEFAULT 0;
    START TRANSACTION;
    INSERT IGNORE INTO success_killed (seckill_id, user_phone,create_time) VALUES (v_seckill_id, v_phone,v_kill_time);
    SELECT ROW_COUNT() INTO insert_count;
    IF (insert_count = 0) THEN
      ROLLBACK;
      SET r_result = -1;
    ELSEIF (insert_count < 0) THEN
      ROLLBACK;
      SET r_result = -2;
    ELSE
      UPDATE seckill SET number = number - 1 WHERE seckill_id = v_seckill_id AND end_time > v_kill_time AND start_time < v_kill_time AND number > 0;
      SELECT ROW_COUNT() INTO insert_count;
      IF (insert_count = 0) THEN
        ROLLBACK;
        SET r_result = 0;
      ELSEIF (insert_count < 0) THEN
        ROLLBACK;
        SET r_result = -2;
      ELSE
        COMMIT;
        SET r_result = 1;
      END IF;
    END IF;
  END;
$$
-- 存储过程定义结束		

-- 修改换行符回来
DELIMITER ;

-- 使用@修改自定义声明的 变量
set @r_result = -3; 

call execute_seckill(1,11112222333,now(),@r_result); 
			
-- 查看结果
select @r_result;			

-- 存储过程是 优化事物行级锁 持有的时间
-- 把事物交给mysql 处理 避免了 避免网络延迟 GC偶尔出现 
			
			
		