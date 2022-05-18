package com.example.demo.entity;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class Employee implements UserDetails {
    private long employee_id;        // 社員ID
    private String name;             // 名前
    private long belong_id;          // 所属先ID
    private String belong_name;      // 所属先名
    private String mail_address;     // メールアドレス
    private String password;         // パスワード
    private String management;       // 管理者権限
    private long deleted_datatime;   // 削除日時
    private String image_file_path;  // 写真ファイルパス
    private MultipartFile multipartFile;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getUsername() {
        return mail_address;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}