package org.example.intellibookmallapi.entity;

import java.util.Date;

/**
 * 用户实体类
 */
public class User {
    
    private Long userId;
    private String username;
    private String password;
    private String email;
    private String nickname;
    private Integer isAdmin;
    private Date createTime;
    
    // Getters and Setters
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getNickname() {
        return nickname;
    }
    
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
    
    public Integer getIsAdmin() {
        return isAdmin;
    }
    
    public void setIsAdmin(Integer isAdmin) {
        this.isAdmin = isAdmin;
    }
    
    public Date getCreateTime() {
        return createTime;
    }
    
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    
    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", nickname='" + nickname + '\'' +
                ", isAdmin=" + isAdmin +
                ", createTime=" + createTime +
                '}';
    }
}
