package com.example.demo.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.entity.Employee;
import com.example.demo.service.EmployeeService;

@Controller
public class EmployeeController {
    @Autowired
    EmployeeService employeeService;

    // 社員一覧の表示
    @GetMapping("/list")
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
        final boolean mailExists = employeeService.duplicationCheck(employee);
        // メールアドレスが存在しないなら登録する
        if (!mailExists) {
            employeeService.create(employee);
        } else {
            return "/create";
        }

        final MultipartFile file = employee.getMultipartFile();
        String fileName = file.getOriginalFilename();

        // ファイルが存在するなら保存する
        if (fileName != "") {
            // ファイル名を 「社員ID + 拡張子」にする。
            String employee_id = employeeService.mailSearch(employee.getMail_address());
            String extension = fileName.substring(fileName.lastIndexOf("."));
            fileName = employee_id + extension;

            // TODO: もっと良いパスの指定方法を調べる
            final Path filePath = Paths.get("C:/pleiades/2022-03/workspace/example-1/src/main/resources/static/images/" + fileName);
            saveFile(file, filePath);
            
            // TODO: 書き直すべき
            employee.setImage_file_path(fileName);
            employee.setEmployee_id(Long.parseLong(employee_id));
            employeeService.update(employee);
        }

        return "redirect:/list";
    }

    // 社員の編集
    @PostMapping("/update")
    public String update(Employee employee) {
        final boolean mailExists = employeeService.duplicationCheck(employee);
        // メールアドレスが存在しないなら編集する
        if (mailExists) {
            return "/update";
        }

        final MultipartFile file = employee.getMultipartFile();
        String fileName = file.getOriginalFilename();

        // ファイルが存在するなら保存する
        if (fileName != "") {
            // ファイル名を 「社員ID + 拡張子」にする。
            String employee_id = employeeService.mailSearch(employee.getMail_address());
            String extension = fileName.substring(fileName.lastIndexOf("."));
            fileName = employee_id + extension;

            // TODO: もっと良いパスの指定方法を調べる
            final Path filePath = Paths.get("C:/pleiades/2022-03/workspace/example-1/src/main/resources/static/images/" + fileName);
            saveFile(file, filePath);
            employee.setImage_file_path(fileName);
        }

        employeeService.update(employee);
        return "redirect:/list";
    }

    // 指定パスにファイルを保存する
    private void saveFile(MultipartFile file, Path filePath) {
        try {
            byte[] bytes = file.getBytes();
            OutputStream stream = Files.newOutputStream(filePath);
            stream.write(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}