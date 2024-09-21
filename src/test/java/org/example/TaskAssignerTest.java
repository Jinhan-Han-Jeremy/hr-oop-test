package org.example;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TaskAssignerTest {

    @Test
    void name() {
        TaskAssigner sut = new TaskAssigner();

        List<Task> task=new ArrayList<>();
        List<String> selectedttask = new ArrayList<>();
        List<TasksHistory> historylist = new ArrayList<>();
        List<Task> tasks = sut.taskAssigner(task, selectedttask, historylist);
        Assertions.assertEquals(new ArrayList<>(), tasks);

    }
}