package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Employee;
import com.example.demo.mapper.EmployeeMapper;

@Service
public class EmployeeService implements UserDetailsService {
    @Autowired
    private EmployeeMapper employeeMapper;

    // ログイン認証用
    @Override
    public UserDetails loadUserByUsername(String mail_address) throws UsernameNotFoundException {
        return employeeMapper.certificate(mail_address);
    }

    // 社員一覧取得
    public List<Employee> selectAll() {
        return employeeMapper.selectAll();
    }

    // 社員の検索（名前）
    public List<Employee> nameSearch(String name) {
        return employeeMapper.nameSearch(name);
    }

    // 社員の検索（社員ID）
    public Employee idSearch(long employee_id) {
        return employeeMapper.idSearch(employee_id);
    }
    
    // 社員の検索（メールアドレス）
    public String mailSearch(String mail_address) {
        return employeeMapper.mailSearch(mail_address);
    }

    // 社員追加
    public void create(Employee employee) {
        employeeMapper.create(employee);
    }

    // 社員削除
    public void delete(long employee_id) {
        Employee employee = employeeMapper.idSearch(employee_id);
        
        // FIXME: ログインユーザーが権限が持っていない場合URLから削除できてしまう。
        // 削除するユーザーの権限がnullだったら削除する
        if (employee.getManagement() == null) {
            employeeMapper.delete(employee_id);
        }
    }

    // 社員情報更新
    public void update(Employee employee) {
        employeeMapper.update(employee);
    }

    // メールアドレス重複チェック(true...重複あり, false...重複なし）
    public boolean duplicationCheck(Employee employee) {
        final String employee_id = employeeMapper.duplicationCheck(employee.getMail_address());

        // 社員IDがnullか変更ユーザーと一致したらfalse
        if (employee_id == null) {
            return false;
        } else if (employee.getEmployee_id() == Long.parseLong(employee_id)) {
            return false;
        }
        return true;
    }
}