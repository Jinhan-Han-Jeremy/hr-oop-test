package org.example;

import java.util.List;

public class TaskAssignmentResult {
    double completionTime;
    List<String> assignedMembers;

    TaskAssignmentResult(double completionTime, List<String> assignedMembers) {
        this.completionTime = completionTime;
        this.assignedMembers = assignedMembers;
    }
}