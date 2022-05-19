package com.example.demo.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.example.demo.entity.Employee;

@Mapper
public interface EmployeeMapper {
    // 社員リストを取得
    @Select("select * from employee_tbl "
            + "inner join belong_master_tbl "
            + "on employee_tbl.belong_id = belong_master_tbl.belong_id "
            + "order by management, employee_id asc")
    public List<Employee> selectAll();

    // 社員の検索（名前）
    @Select("select * from employee_tbl "
            + "inner join belong_master_tbl "
            + "on employee_tbl.belong_id = belong_master_tbl.belong_id "
            + "where name like concat('%', #{name}, '%') "
            + "order by management, employee_id asc")
    public List<Employee> nameSearch(String name);

    // 社員の検索（社員ID）
    @Select("select * from employee_tbl where employee_id = #{employee_id}")
    public Employee idSearch(long employee_id);
    
    // 社員の検索（メールアドレス）
    @Select("select employee_id from employee_tbl where mail_address = #{mail_address}")
    public String mailSearch(String mail_address);

    // 社員の追加
    @Insert("insert into employee_tbl ("
            + "name"
            + ", belong_id"
            + ", mail_address"
            + ", password"
            + ", management"
            + ", image_file_path"
            + ") values ("
            + "#{name}"
            + ", #{belong_id}"
            + ", #{mail_address}"
            + ", #{password}"
            + ", #{management}"
            + ", #{image_file_path}"
            + ")")
    public void create(Employee employee);

    // 社員の削除
    @Delete("delete from employee_tbl where employee_id = #{employee_id}")
    public void delete(long employee_id);

    // 社員の更新
    @Update("update employee_tbl set "
            + "name = #{name}"
            + ", belong_id = #{belong_id}"
            + ", mail_address = #{mail_address}"
            + ", password = #{password}"
            + ", management = #{management}"
            + ", image_file_path = #{image_file_path}"
            + "where employee_id = #{employee_id}")
    public void update(Employee employee);

    // ログイン認証用
    @Select("select * from employee_tbl where mail_address = #{mail_address}")
    public Employee certificate(@Param("mail_address") String mail_address);

    // メールアドレス重複チェック
    @Select("select employee_id from employee_tbl where mail_address = #{mail_address}")
    public String duplicationCheck(String mail_address);
}