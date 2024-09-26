package com.airwin.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.airwin.dto.UserDTO;
import com.fasterxml.jackson.annotation.JsonProperty;


@Table(name = "users")
public class User implements UserDetails {
    @Id
    private Integer userid;
    private String username;
    private String password;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private UserAuthority authority;
    public User() {
        
    }
    public User(UserDTO userDTO) {
        this.setUserid(userDTO.getUserid());
        this.setUsername(userDTO.getUsername());
        this.setPassword(userDTO.getPassword());
        this.setAuthority(UserAuthority.valueOf(userDTO.getAuthority()));
    }
    public Integer getUserid() {
        return userid;
    }

    public void setUserid(int id) {
        this.userid = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setAuthority(UserAuthority authority) {
        this.authority = authority;
    }
@Override
public Collection<? extends GrantedAuthority> getAuthorities() {
    List<GrantedAuthority> authorities = new ArrayList<>();
    authorities.add(new SimpleGrantedAuthority(authority.name()));
    return authorities;
}

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

}
