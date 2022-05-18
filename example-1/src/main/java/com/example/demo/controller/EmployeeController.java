package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.entity.Employee;
import com.example.demo.service.EmployeeService;

@Controller
public class EmployeeController {
    @Autowired
    EmployeeService employeeService;

    // 社員一覧の表示
    @GetMapping({"/", "/list"})
    public String displayList(Model model) {
        List<Employee> list = employeeService.selectAll();
        model.addAttribute("employeeList", list);
        return "list";
    }

    // 社員追加の表示
    @GetMapping("/add")
    public String displayAdd() {
        return "add";
    }

    // 社員編集の表示
    @GetMapping("/edit")
    public String displayEdit(long employee_id, Model model) {
        Employee employee = employeeService.idSearch(employee_id);
        model.addAttribute("employee", employee);
        return "edit";
    }

    // 社員の検索（名前から）
    @GetMapping("/name_search")
    public String nameSearch(String name, Model model) {
        List<Employee> list = employeeService.nameSearch(name);
        model.addAttribute("employeeList", list);
        return "list";
    }

    // 社員の削除
    @GetMapping("/delete")
    public String deleteEmployee(long employee_id) {
        employeeService.delete(employee_id);
        return "redirect:/list";
    }

    // 社員の登録
    @PostMapping("/create")
    public String createEmployee(Employee employee) {
        boolean mailExists = employeeService.duplicationCheck(employee);
        // メールアドレスが存在しないなら登録する
        if (!mailExists) {
            employeeService.create(employee);
        }

        return "redirect:/list";
    }

    // 社員の編集
    @PostMapping("/update")
    public String update(Employee employee) {
        boolean mailExists = employeeService.duplicationCheck(employee); 
        // メールアドレスが存在しないなら編集する
        if (!mailExists) {
            employeeService.update(employee);
        }

        return "redirect:/list";
    }
}