package org.example;

import java.util.List;

public interface TasksHistoryInterface {
    String getName();
    List<String> getTeamMembers();
    List<String> getAvailableJobs();
    int getFinishedDays();
    String getState();
    boolean isRequirementsSatisfied();

    void setName(String name);
    void setTeamMembers(List<String> teamMembers);
    void setAvailableJobs(List<String> availableJobs);
    void setFinishedDays(int finishedDays);
    void setState(String state);
    void setRequirementsSatisfied(boolean requirementsSatisfied);
}