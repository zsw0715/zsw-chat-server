package com.example.zsw_chat_server.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import lombok.Data;


@Data
@TableName("friend")
public class Friend implements Serializable {
	@TableId(type = IdType.AUTO)
	private Long id;

	private Long userId;
    private Long friendUid;

	@TableField("become_friend_time")
    private LocalDateTime createTime;

	@TableField("friend_nickname")
	private String remark;

}
