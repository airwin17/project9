package com.airwin.dto;

import com.airwin.model.User;

public class UserDTO {

    private int userid;
    private String username;
    private String password;
    private String authority;
    public UserDTO() {
        
    }
    public UserDTO(User user) {
        userid=user.getUserid();
        username=user.getUsername();
        authority=user.getAuthorities().stream().findFirst().get().getAuthority();
    }
    
    public int getUserid() {
        return userid;
    }
    public void setUserid(int userid) {
        this.userid = userid;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getAuthority() {
        return authority;
    }
    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
}
