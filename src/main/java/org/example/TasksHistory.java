package org.example;

import java.util.List;

public class TasksHistory implements TasksHistoryInterface {
    private String name;
    private List<String> teamMembers;
    private List<String> availableJobs;
    private int finishedDays;
    private String state;
    private boolean requirementsSatisfied;

    public TasksHistory(String name, List<String> teamMembers, List<String> availableJobs, int finishedDays, String state, boolean requirementsSatisfied) {
        this.name = name;
        this.teamMembers = teamMembers;
        this.availableJobs = availableJobs;
        this.finishedDays = finishedDays;
        this.state = state;
        this.requirementsSatisfied = requirementsSatisfied;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public List<String> getTeamMembers() {
        return teamMembers;
    }

    @Override
    public List<String> getAvailableJobs() {
        return availableJobs;
    }

    @Override
    public int getFinishedDays() {
        return finishedDays;
    }

    @Override
    public String getState() {
        return state;
    }

    @Override
    public boolean isRequirementsSatisfied() {
        return requirementsSatisfied;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void setTeamMembers(List<String> teamMembers) {
        this.teamMembers = teamMembers;
    }

    @Override
    public void setAvailableJobs(List<String> availableJobs) {
        this.availableJobs = availableJobs;
    }

    @Override
    public void setFinishedDays(int finishedDays) {
        this.finishedDays = finishedDays;
    }

    @Override
    public void setState(String state) {
        this.state = state;
    }

    @Override
    public void setRequirementsSatisfied(boolean requirementsSatisfied) {
        this.requirementsSatisfied = requirementsSatisfied;
    }

    @Override
    public String toString() {
        return "TasksHistory{" +
                "name='" + name + '\'' +
                ", teamMembers=" + teamMembers +
                ", availableJobs=" + availableJobs +
                ", finishedDays=" + finishedDays +
                ", state='" + state + '\'' +
                ", requirementsSatisfied=" + requirementsSatisfied +
                '}';
    }
}
