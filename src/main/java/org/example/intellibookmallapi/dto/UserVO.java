package org.example.intellibookmallapi.dto;

/**
 * 用户信息VO（返回给前端，不包含密码）
 */
public class UserVO {
    
    private Long userId;
    private String username;
    private String email;
    private String nickname;
    private Integer isAdmin;
    
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
}
