package org.seckill.entity;


import java.util.Date;

public class Successkilled {
    private Long seckillId;

    private Long userPhone;

    private Byte start;

    private Date createTime;
    
    
    // 多对一的关系
    private  Seckill seckill;

    public Seckill getSeckill() {
		return seckill;
	}

	public void setSeckill(Seckill seckill) {
		this.seckill = seckill;
	}

	public Long getSeckillId() {
        return seckillId;
    }

    public void setSeckillId(Long seckillId) {
        this.seckillId = seckillId;
    }

    public Long getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(Long userPhone) {
        this.userPhone = userPhone;
    }

    public Byte getStart() {
        return start;
    }

    public void setStart(Byte start) {
        this.start = start;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}