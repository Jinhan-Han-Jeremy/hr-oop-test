package org.example;

import java.util.List;

public class Task implements TaskInterface {
    private String name;  // 작업 이름
    private List<String> employees;  // 작업을 수행할 수 있는 직원들의 역할
    private int difficulty;  // 작업 난이도

    // 생성자
    public Task(String name, List<String> employees, int difficulty) {
        this.name = name;
        this.employees = employees;
        this.difficulty = difficulty;
    }

    // getter 메서드
    public String getName() {
        return name;
    }

    public List<String> getEmployees() {
        return employees;
    }

    public int getDifficulty() {
        return difficulty;
    }


}