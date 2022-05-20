package com.example.demo.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

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
        final List<Employee> list = employeeService.selectAll();
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
        final Employee employee = employeeService.idSearch(employee_id);
        model.addAttribute("employee", employee);
        return "edit";
    }

    // 社員の検索（名前から）
    @GetMapping("/name_search")
    public String nameSearch(String name, Model model) {
        final List<Employee> list = employeeService.nameSearch(name);
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
        // メールアドレスが重複しているなら戻る
        if (mailExists) {
            return "add";
        }

        final MultipartFile file = employee.getMultipartFile();
        // ファイルが存在するなら保存する
        if (!file.isEmpty()) {
            // ファイル名を 「UUID + 拡張子」にする。
            String fileName = file.getOriginalFilename();
            String extension = fileName.substring(fileName.lastIndexOf("."));
            fileName = UUID.randomUUID() + extension;

            // 保存パスの指定
            final Path filePath = Paths.get("src/main/resources/static/images/" + fileName);
            saveFile(file, filePath);
            employee.setImage_file_path(fileName);
        }
        
        employeeService.create(employee);

        return "redirect:/list";
    }

    // 社員の編集
    @PostMapping("/update")
    public String update(Employee employee) {
        final boolean mailExists = employeeService.duplicationCheck(employee);
        // メールアドレスが重複しているなら戻る
        if (mailExists) {
            return "edit";
        }

        final MultipartFile file = employee.getMultipartFile();
        // ファイルが存在するなら保存する
        if (!file.isEmpty()) {
            // ファイル名を 「UUID + 拡張子」にする。
            String fileName = file.getOriginalFilename();
            String extension = fileName.substring(fileName.lastIndexOf("."));
            fileName = UUID.randomUUID() + extension;

            // 保存パスの指定
            final Path filePath = Paths.get("src/main/resources/static/images/" + fileName);
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