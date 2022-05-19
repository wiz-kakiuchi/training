package com.example.demo.entity;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
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

    // ユーザーに付与された権限を返す
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (management == null) {
            return AuthorityUtils.createAuthorityList("USER");
        }
        return AuthorityUtils.createAuthorityList("ADMIN");
    }

    // ユーザー認証に使用されるユーザ名を返す
    @Override
    public String getUsername() {
        return mail_address;
    }

    // ユーザーの有効期限が切れているか
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // ユーザーがロックされているか
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // ユーザーのパスワードの有効期限が切れているか
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // ユーザーが有効か無効か
    @Override
    public boolean isEnabled() {
        return true;
    }
}