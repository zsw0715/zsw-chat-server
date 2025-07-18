package com.example.zsw_chat_server.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import lombok.Data;

@Data
@TableName("users")
public class User implements Serializable{
    @TableId(type = IdType.AUTO)
	private Long uid;
    private String username;
    private String password;
    private String email;
	@TableField("created_at")
    private LocalDateTime created_at;
	@TableField("last_login_at")
    private LocalDateTime last_login_at;
    private Integer gender;
}
