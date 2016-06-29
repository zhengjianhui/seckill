package org.seckill.entity;


import java.util.Date;

public class Seckill {
    private String name;

    private Integer number;
    
    private Long seckillId;

    private Date startTime;

    private Date endTime;

    private Date createTime;

    public Long getSeckillId() {
        return seckillId;
    }

    public void setSeckillId(Long seckillId) {
        this.seckillId = seckillId;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

	@Override
	public String toString() {
		return "Seckill [name=" + name + ", number=" + number + ", seckillId="
				+ seckillId + ", startTime=" + startTime + ", endTime="
				+ endTime + ", createTime=" + createTime + "]";
	}
    
    
}