package org.example;

import java.util.*;

public class TaskAssigner {

    // Levenshtein Distance 계산 메서드
    private int levenshteinDistance(String s1, String s2) {
        int[][] dp = new int[s1.length() + 1][s2.length() + 1];

        for (int i = 0; i <= s1.length(); i++) {
            for (int j = 0; j <= s2.length(); j++) {
                if (i == 0) {
                    dp[i][j] = j;
                } else if (j == 0) {
                    dp[i][j] = i;
                } else {
                    int cost = (s1.charAt(i - 1) == s2.charAt(j - 1)) ? 0 : 1;
                    dp[i][j] = Math.min(Math.min(dp[i - 1][j] + 1, dp[i][j - 1] + 1), dp[i - 1][j - 1] + cost);
                }
            }
        }

        return dp[s1.length()][s2.length()];
    }

    // 두 문자열 간 유사도 계산
    private double similarity(String s1, String s2) {
        int maxLength = Math.max(s1.length(), s2.length());
        if (maxLength == 0) {
            return 1.0; // 두 문자열이 모두 비어있을 때 유사도는 100%
        }
        int distance = levenshteinDistance(s1, s2);
        return 1.0 - ((double) distance / maxLength);
    }

    // 유사성이 75% 이상이면 true를 반환하는 메서드
    private boolean isSimilar(String selectedTaskNames, String theWord) {
        double threshold = 0.75;  // 유사도 기준 75%
        double similarityValue = similarity(selectedTaskNames, theWord);
        return similarityValue >= threshold;
    }

    // List<TasksHistory>와 Task를 비교하는 함수
    private boolean checkTaskState(Task task, List<TasksHistory> tasksHistoryList) {
        for (TasksHistory tasksHistory : tasksHistoryList) {
            // task 이름과 TasksHistory 이름이 같고, state가 "done"인 경우 false 반환
            if (task.getName().equals(tasksHistory.getName()) && "done".equals(tasksHistory.getState())) {
                return false;
            }
        }
        // 조건에 해당하지 않으면 true 반환
        return true;
    }

    private String removeWords(String taskName) {
        // " 초기"로 끝나면 " 초기" 제거
        if (taskName.endsWith(" 초기")) {
            taskName = taskName.substring(0, taskName.length() - 3);  // " 초기"의 길이는 3이므로 마지막 3글자를 제거
        }
        // " 후기"로 끝나면 " 후기" 제거
        else if (taskName.endsWith(" 후기")) {
            taskName = taskName.substring(0, taskName.length() - 3);  // " 후기"의 길이도 3이므로 마지막 3글자를 제거
        }

        return taskName;
    }

    // 두 문자열 중 하나가 다른 하나를 포함하는지 확인하는 함수
    private boolean containsOrContained(String selectedTaskName, String theWord) {
        // selectedTaskName이 theWord를 포함하거나 theWord가 selectedTaskName을 포함하면 true 반환
        return selectedTaskName.contains(theWord) || theWord.contains(selectedTaskName);
    }

    public List<Task> taskAssigner(List<Task> tasks, List<String> selectedTaskNames, List<TasksHistory> tasksHistoryList){

        List<Task> selectedTasks = new ArrayList<>();
        Set<String> words = new HashSet<>();
        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.get(i);

            for(String selectedTaskName : selectedTaskNames){
                String theWord = removeWords(task.getName());
                words.add(theWord);
                System.out.println(i + " tsk " + selectedTaskName + "  select task " + theWord);

                // 현재 작업 리스트에 올라와 있지 않은 작업인가? 그리고 한번 노출 된적 있던 작업인가?
                if(checkTaskState(task, tasksHistoryList) && words.contains(theWord)){
                    // 유사성 판단 결과 출력
                    if (isSimilar(selectedTaskName, theWord) || containsOrContained(selectedTaskName, theWord)) {
                        System.out.println("두 문자열이 75% 이상 유사하고 해당 단어를 포함하고 있습니다"+theWord);

                        selectedTasks.add(task);
                    }
                }
            }
        }

        selectedTasks.sort(Comparator.comparingInt(Task::getDifficulty).reversed());
        return selectedTasks;
    }
}
