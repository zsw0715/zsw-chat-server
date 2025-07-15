package com.example.zsw_chat_server.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("users")
public class User implements Serializable{
	private Long uid;
    private String username;
    private String password;
    private String email;
    private LocalDateTime created_at;
    private LocalDateTime last_login_at;
    private Integer gender;
}
