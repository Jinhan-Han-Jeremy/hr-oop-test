package org.example;

import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.*;
import java.math.RoundingMode;

public class Main {
    public static List<TeamMember> findOptimalTeamForTask2(List<TeamMember> teamMembers, double[][] timeMatrix, int taskIdx) {
        BigDecimal bestTime = new BigDecimal(Double.MAX_VALUE);
        List<TeamMember> bestCombination = new ArrayList<>();

        // 3명의 팀 멤버를 선택하는 모든 조합을 생성
        for (int i = 0; i < teamMembers.size() - 2; i++) {
            for (int j = i + 1; j < teamMembers.size() - 1; j++) {
                for (int k = j + 1; k < teamMembers.size(); k++) {
                    double time1 = timeMatrix[i][taskIdx];
                    double time2 = timeMatrix[j][taskIdx];
                    double time3 = timeMatrix[k][taskIdx];

                    // 유효한 작업 시간에 대해서만 계산
                    if (time1 > 0 && time2 > 0 && time3 > 0 && Double.isFinite(time1) && Double.isFinite(time2) && Double.isFinite(time3)) {
                        BigDecimal parallelTime = BigDecimal.valueOf(3.0 / (1.0 / time1 + 1.0 / time2 + 1.0 / time3));

                        if (parallelTime.compareTo(bestTime) < 0) {
                            bestTime = parallelTime;
                            bestCombination = Arrays.asList(teamMembers.get(i), teamMembers.get(j), teamMembers.get(k));
                        }
                    }
                    // 2명의 팀원만 유효할 때 병렬 계산
                    else if (time1 > 0 && time2 > 0 && Double.isFinite(time1) && Double.isFinite(time2)) {
                        BigDecimal parallelTime = BigDecimal.valueOf(2.0 / (1.0 / time1 + 1.0 / time2));

                        if (parallelTime.compareTo(bestTime) < 0) {
                            bestTime = parallelTime;
                            bestCombination = Arrays.asList(teamMembers.get(i), teamMembers.get(j));
                        }
                    }
                    // 1명의 팀원만 유효할 때
                    else if (time1 > 0 && Double.isFinite(time1)) {
                        BigDecimal taskTime = BigDecimal.valueOf(time1);

                        if (taskTime.compareTo(bestTime) < 0) {
                            bestTime = taskTime;
                            bestCombination = Arrays.asList(teamMembers.get(i));
                        }
                    }
                }
            }
        }
        return bestCombination;
    }
    // 최적의 팀을 찾아 작업 시간을 출력하는 함수
    public static void findOptimalTeamCombination2(List<TeamMember> teamMembers, double[][] timeMatrix, List<String> tasks, int firstTaskIdx) {
        // 첫 번째 작업에 대한 팀 구성 및 작업 시간 계산
        List<TeamMember> firstTaskTeam = findOptimalTeamForTask2(teamMembers, timeMatrix, firstTaskIdx);
        BigDecimal firstTaskTime = calculateTaskTime2(teamMembers, firstTaskTeam, timeMatrix, firstTaskIdx);
        List<TeamMember> remainingMembers = excludeTeamMembers2(teamMembers, firstTaskTeam);

        // 두 번째 작업
        List<TeamMember> secondTaskTeam = findOptimalTeamForTask2(remainingMembers, timeMatrix, (firstTaskIdx + 1) % tasks.size());
        BigDecimal secondTaskTime = calculateTaskTime2(remainingMembers, secondTaskTeam, timeMatrix, (firstTaskIdx + 1) % tasks.size());
        List<TeamMember> remainingMembersAfterSecond = excludeTeamMembers2(remainingMembers, secondTaskTeam);

        // 세 번째 작업
        List<TeamMember> thirdTaskTeam = findOptimalTeamForTask2(remainingMembersAfterSecond, timeMatrix, (firstTaskIdx + 2) % tasks.size());
        BigDecimal thirdTaskTime = calculateTaskTime2(remainingMembersAfterSecond, thirdTaskTeam, timeMatrix, (firstTaskIdx + 2) % tasks.size());

        List<String> firstTeamNames = new ArrayList<>();
        for (int i =0; i < firstTaskTeam.size(); i++) {
            firstTeamNames.add(firstTaskTeam.get(i).getName());
        }
        // 결과 출력
        System.out.println("First task " + tasks.get(firstTaskIdx) + ": " + firstTeamNames + ", Completion time: " + String.format("%.2f", firstTaskTime) + " days");
        System.out.println("Second task " + tasks.get((firstTaskIdx + 1) % tasks.size()) + ": " + secondTaskTeam + ", Completion time: " + String.format("%.2f", secondTaskTime) + " days");
        System.out.println("Third task " + tasks.get((firstTaskIdx + 2) % tasks.size()) + ": " + thirdTaskTeam + ", Completion time: " + String.format("%.2f", thirdTaskTime) + " days");
    }

    // 두 번째 함수: 병렬로 작업 시간을 계산
    public static BigDecimal calculateParallelTime(List<Double> times) {
        //BigDecimal BigInteger
        BigDecimal sum = new BigDecimal(0.0);
        for (double time : times) {
            BigDecimal timeBD = BigDecimal.valueOf(time);
            if (timeBD.compareTo(BigDecimal.ZERO) > 0) { // time이 0보다 클 때만 처리
                sum = sum.add(BigDecimal.ONE.divide(timeBD, 3, RoundingMode.HALF_UP)); // 역수 더하기
            }
        }

        // 병렬 작업 시간 계산 (3.0을 BigDecimal로 변환하여 사용)
        if (sum.compareTo(BigDecimal.ZERO) > 0) {
            return BigDecimal.valueOf(3.0).divide(sum, 3, RoundingMode.HALF_UP); // 소수점 10자리까지 반올림
        } else {
            return BigDecimal.ZERO; // 계산할 수 없을 경우 0 반환
        }
    }
    // 작업 시간을 계산하는 함수 (선택된 팀 멤버의 병렬 작업 시간)
    public static BigDecimal calculateTaskTime2(List<TeamMember> teamMembers, List<TeamMember> selectedTeam, double[][] timeMatrix, int taskIdx) {
        List<Double> times = new ArrayList<>();
        for (TeamMember member : selectedTeam) {
            for (int i = 0; i < teamMembers.size(); i++) {
                if (teamMembers.get(i).getName().equals(member)) {
                    times.add(timeMatrix[i][taskIdx]);
                    break;
                }
            }
        }
        // 병렬 작업 시간 계산
        return calculateParallelTime(times);
    }

    // 특정 작업에 할당된 팀원들을 제외한 나머지 팀원을 반환하는 함수
    public static List<TeamMember> excludeTeamMembers2(List<TeamMember> allMembers, List<TeamMember>  selectedMembers) {
        List<TeamMember> remainings = new ArrayList<>();
        for (TeamMember member : allMembers) {
            if (!selectedMembers.contains(member)) {
                remainings.add(member);
            }
        }

        return remainings;
    }

    // 전체 작업 순서에 대해 최적의 멤버 조합을 구하고, 작업 시간을 출력하는 함수
    public static void findOptimalTeamCombination(String[] teamMembers, double[][] timeMatrix, List<String> tasks, int firstTaskIdx) {
        // 첫 번째 작업에 대해 최적의 팀을 선택하고 작업 시간을 계산
        List<String> firstTaskTeam = findOptimalTeamForTask(teamMembers, timeMatrix, firstTaskIdx);
        BigDecimal firstTaskTime = calculateTaskTime(teamMembers, firstTaskTeam, timeMatrix, firstTaskIdx);

        // 첫 번째 작업에 선택된 팀원 제외
        String[] remainingMembers = excludeTeamMembers(teamMembers, firstTaskTeam);

        // 두 번째 작업
        List<String> secondTaskTeam = findOptimalTeamForTask(remainingMembers, timeMatrix, (firstTaskIdx + 1) % tasks.size());
        BigDecimal secondTaskTime = calculateTaskTime(remainingMembers, secondTaskTeam, timeMatrix, (firstTaskIdx + 1) % tasks.size());
        String[] remainingMembersAfterSecond = excludeTeamMembers(remainingMembers, secondTaskTeam);

        // 세 번째 작업
        List<String> thirdTaskTeam = findOptimalTeamForTask(remainingMembersAfterSecond, timeMatrix, (firstTaskIdx + 2) % tasks.size());
        BigDecimal thirdTaskTime = calculateTaskTime(remainingMembersAfterSecond, thirdTaskTeam, timeMatrix, (firstTaskIdx + 2) % tasks.size());

        // 결과 출력
        System.out.println("First task " + tasks.get(firstTaskIdx) + ": " + firstTaskTeam + ", Completion time: " + String.format("%.2f", firstTaskTime) + " days");
        System.out.println("Second task " + tasks.get((firstTaskIdx + 1) % tasks.size()) + ": " + secondTaskTeam + ", Completion time: " + String.format("%.2f", secondTaskTime) + " days");
        System.out.println("Third task " + tasks.get((firstTaskIdx + 2) % tasks.size()) + ": " + thirdTaskTeam + ", Completion time: " + String.format("%.2f", thirdTaskTime) + " days");
    }


    public static List<String> findOptimalTeamForTask(String[] teamMembers, double[][] timeMatrix, int taskIdx) {
        double bestTime = Double.POSITIVE_INFINITY;
        List<String> bestCombination = new ArrayList<>();

        // 3명의 팀 멤버를 선택하는 모든 조합을 생성
        for (int i = 0; i < teamMembers.length - 2; i++) {
            for (int j = i + 1; j < teamMembers.length - 1; j++) {
                for (int k = j + 1; k < teamMembers.length; k++) {
                    double time1 = timeMatrix[i][taskIdx];
                    double time2 = timeMatrix[j][taskIdx];
                    double time3 = timeMatrix[k][taskIdx];

                    double parallelTime = 0.0;

                    // 유효한 작업 시간에 대해서만 계산
                    if (Double.isFinite(time1) && Double.isFinite(time2) && Double.isFinite(time3)) {
                        // 3명이 모두 작업 가능할 때 병렬 작업 시간 계산
                        parallelTime = 3.0 / (1.0 / time1 + 1.0 / time2 + 1.0 / time3);

                        if (parallelTime < bestTime) {
                            bestTime = parallelTime;
                            bestCombination = Arrays.asList(teamMembers[i], teamMembers[j], teamMembers[k]);
                        }
                    }
                    // 2명만 작업 가능할 때 병렬 작업 시간 계산
                    else if (Double.isFinite(time1) && Double.isFinite(time2)) {
                        parallelTime = 2.0 / (1.0 / time1 + 1.0 / time2);

                        if (parallelTime < bestTime) {
                            bestTime = parallelTime;
                            bestCombination = Arrays.asList(teamMembers[i], teamMembers[j]);
                        }
                    }
                    // 1명만 작업 가능할 때 작업 시간 그대로 사용
                    else if (Double.isFinite(time1)) {
                        parallelTime = time1;

                        if (parallelTime < bestTime) {
                            bestTime = parallelTime;
                            bestCombination = Arrays.asList(teamMembers[i]);
                        }
                    }
                }
            }
        }
        return bestCombination;
    }
    // 작업 시간을 계산하는 함수 (선택된 팀 멤버의 병렬 작업 시간)
    public static BigDecimal calculateTaskTime(String[] teamMembers, List<String> selectedMembers, double[][] timeMatrix, int taskIdx) {
        List<Double> times = new ArrayList<>();
        for (String member : selectedMembers) {
            for (int i = 0; i < teamMembers.length; i++) {
                if (teamMembers[i].equals(member)) {
                    times.add(timeMatrix[i][taskIdx]);
                    break;
                }
            }
        }
        // 병렬 작업 시간 계산
        return calculateParallelTime(times);
    }

    // 특정 작업에 할당된 팀원들을 제외한 나머지 팀원을 반환하는 함수
    public static String[] excludeTeamMembers(String[] allMembers, List<String> selectedMembers) {
        List<String> remaining = new ArrayList<>();
        for (String member : allMembers) {
            if (!selectedMembers.contains(member)) {
                remaining.add(member);
            }
        }
        return remaining.toArray(new String[0]);
    }

    public static void hashMapSplitter(Map<String, Integer> hashMapStrInt){

        for (String key : hashMapStrInt.keySet()) {
            Integer value = hashMapStrInt.get(key);
            System.out.println("작업 : " + key + ", 값 : " + value);
        }
    }

    public static String removeWords(String taskName) {
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

    public static List<Task> taskAssigner(List<Task> tasks, List<String> selectedTaskNames, List<TasksHistory> tasksHistoryList){

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

    //select member
    // 추가가능한 멤버를 필터
    private static boolean addableMemberFilter(int taskDifficulty, int memberLevel, boolean userOrder) {

        //유저의 강제 할당 실행
        if (userOrder == true) {
            return true; // 난이도가 높고 멤버의 레벨이 1 이상이면 추가
        }
        //difficulty에 따른 적합한 레벨의 유저 할당
        if (taskDifficulty >= 3 && memberLevel > 1) {
            return true; // 난이도가 높고 멤버의 레벨이 1 이상이면 추가
        } else if (taskDifficulty < 3 && memberLevel < 3) {
            return true; // 난이도가 낮고 멤버의 레벨이 3 미만이면 추가
        }
        return false;
    }

    // 멤버가 선택된 작업을 수행할 수 있는지 확인하는 함수
    private static boolean canAddMemberForTasks(TeamMember member, List<Task> tasks, boolean userOrder) {
        Map<String, Integer> evaluations = member.getEvaluations(); // 각 멤버의 가능 작업 (작업당 소요 시간)

        for (Task task : tasks) {
            String taskName = task.getName();
            int taskDifficulty = task.getDifficulty();
            int memberLevel = member.getLevel();
            if (evaluations.containsKey(taskName) && addableMemberFilter(taskDifficulty, memberLevel, userOrder)) {
                return true; // 작업에 대한 수치가 있으면 true 반환
            }
        }
        return false;
    }

    public static List<TeamMember> selectMember(List<Task> selectedTasks, List<TeamMember> members, boolean userOrder) {
        List<TeamMember> selectedMembers = new ArrayList<>();

        for (TeamMember member : members) {
            boolean addable = canAddMemberForTasks(member, selectedTasks, userOrder); // 멤버가 작업을 수행할 수 있는지 확인

            if (addable && member.getState() == true) {
                selectedMembers.add(member); // 조건에 맞는 멤버를 추가
            }
        }
        return selectedMembers;
    }
    //select member



    // 두 문자열 중 하나가 다른 하나를 포함하는지 확인하는 함수
    public static boolean containsOrContained(String selectedTaskName, String theWord) {
        // selectedTaskName이 theWord를 포함하거나 theWord가 selectedTaskName을 포함하면 true 반환
        return selectedTaskName.contains(theWord) || theWord.contains(selectedTaskName);
    }

    // Levenshtein Distance 계산 메서드
    public static int levenshteinDistance(String s1, String s2) {
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
    public static double similarity(String s1, String s2) {
        int maxLength = Math.max(s1.length(), s2.length());
        if (maxLength == 0) {
            return 1.0; // 두 문자열이 모두 비어있을 때 유사도는 100%
        }
        int distance = levenshteinDistance(s1, s2);
        return 1.0 - ((double) distance / maxLength);
    }

    // 유사성이 75% 이상이면 true를 반환하는 메서드
    public static boolean isSimilar(String selectedTaskNames, String theWord) {
        double threshold = 0.75;  // 유사도 기준 75%
        double similarityValue = similarity(selectedTaskNames, theWord);
        return similarityValue >= threshold;
    }


    // List<TasksHistory>와 Task를 비교하는 함수
    public static boolean checkTaskState(Task task, List<TasksHistory> tasksHistoryList) {
        for (TasksHistory tasksHistory : tasksHistoryList) {
            // task 이름과 TasksHistory 이름이 같고, state가 "done"인 경우 false 반환
            if (task.getName().equals(tasksHistory.getName()) && "done".equals(tasksHistory.getState())) {
                return false;
            }
        }
        // 조건에 해당하지 않으면 true 반환
        return true;
    }


    public static double[][] generateTimeMatrix(List<Task> selectedTasks, List<TeamMember> members) {

        // 시간 행렬 초기화 (팀 멤버 수 x 작업 수)
        double[][] timeMatrix = new double[members.size()][selectedTasks.size()];

        // 각 멤버가 작업을 완료하는 시간을 계산하여 timeMatrix에 저장
        for (int i = 0; i < members.size(); i++) {
            TeamMember member = members.get(i);
            Map<String, Integer> evaluations = member.getEvaluations(); // 각 멤버의 작업 평가 (작업당 소요 시간)

            for (int j = 0; j < selectedTasks.size(); j++) {
                Task task = selectedTasks.get(j);
                String taskName = task.getName();

                // 해당 작업에 대한 평가 점수가 있는지 확인
                if (evaluations.containsKey(taskName)) {
                    int time = evaluations.get(taskName);
                    timeMatrix[i][j] = time; // 작업 시간을 할당
                } else {
                    timeMatrix[i][j] = Double.POSITIVE_INFINITY; // 평가가 없으면 불가능한 작업으로 처리
                }
            }
        }
        return timeMatrix;
    }

    public static void main(String[] args) throws UnsupportedEncodingException {
        // 출력 인코딩을 UTF-8로 설정
        System.setOut(new PrintStream(System.out, true, "UTF-8"));

        List<TeamMember> members = new ArrayList<>();  // 팀 멤버 리스트
        List<Task> tasks = new ArrayList<>();  // 작업 리스트

        // 이프로 - Project Manager
        Map<String, Integer> evaluations1 = new HashMap<>();
        evaluations1.put("프로젝트 목표와 범위 설정 초기", 3);
        evaluations1.put("프로젝트 목표와 범위 설정 후기", 4);
        evaluations1.put("일정과 예산 계획 수립 초기", 4);
        evaluations1.put("일정과 예산 계획 수립 후기", 4);
        evaluations1.put("팀 구성 및 역할 할당 초기", 4);
        evaluations1.put("팀 구성 및 역할 할당 후기", 4);
        members.add(new TeamMember("이프로", "Project Manager", 2, true, new ArrayList<>(evaluations1.keySet()), evaluations1));

        // 최프로 - Project Manager
        Map<String, Integer> evaluations2 = new HashMap<>();
        evaluations2.put("프로젝트 목표와 범위 설정 초기", 3);
        evaluations2.put("프로젝트 목표와 범위 설정 후기", 3);
        evaluations2.put("일정과 예산 계획 수립 초기", 3);
        evaluations2.put("일정과 예산 계획 수립 후기", 4);
        members.add(new TeamMember("최프로", "Project Manager", 3, true, new ArrayList<>(evaluations2.keySet()), evaluations2));

        // 이덕마 - Product Manager
        Map<String, Integer> evaluations3 = new HashMap<>();
        evaluations3.put("팀 구성 및 역할 할당 후기", 4);
        evaluations3.put("프로젝트 킥오프 미팅 주최 초기", 5);
        evaluations3.put("서비스 수익 분석 초기", 3);
        evaluations3.put("프로젝트 킥오프 미팅 주최 후기", 5);
        evaluations3.put("서비스 수익 분석 후기", 3);
        members.add(new TeamMember("이덕마", "Product Manager", 2, false, new ArrayList<>(evaluations3.keySet()), evaluations3));

        // 최덕마 - Product Manager
        Map<String, Integer> evaluations4 = new HashMap<>();
        evaluations4.put("팀 구성 및 역할 할당 초기", 3);
        evaluations4.put("팀 구성 및 역할 할당 후기", 3);
        evaluations4.put("프로젝트 킥오프 미팅 주최 초기", 4);
        evaluations4.put("프로젝트 킥오프 미팅 주최 후기", 4);
        evaluations4.put("서비스 수익 분석 후기", 4);
        evaluations4.put("서비스 수익 분석 초기", 4);
        members.add(new TeamMember("최덕마", "Product Manager", 3, true, new ArrayList<>(evaluations4.keySet()), evaluations4));

        // 이비례 - Business Operator
        Map<String, Integer> evaluations5 = new HashMap<>();
        evaluations5.put("비즈니스 요구사항 분석 초기", 4);
        evaluations5.put("비즈니스 요구사항 분석 후기", 4);
        evaluations5.put("서비스 수익 분석 초기", 3);
        evaluations5.put("프로젝트 킥오프 미팅 주최 후기", 5);
        evaluations5.put("서비스 수익 분석 후기", 3);
        members.add(new TeamMember("이비례", "Business Operator", 2, true, new ArrayList<>(evaluations5.keySet()), evaluations5));

        // 유태리 - Tech Lead
        Map<String, Integer> evaluations6 = new HashMap<>();
        evaluations6.put("기술 스택 선정 초기", 4);
        evaluations6.put("시스템 아키텍처 설계 초기", 4);
        evaluations6.put("RESTful 서비스 초기", 4);
        evaluations6.put("마이크로서비스 초기", 4);
        members.add(new TeamMember("유태리", "Tech Lead", 3, true, new ArrayList<>(evaluations6.keySet()), evaluations6));

        List<String> employees = List.of("ProjectManager", "BusinessOperator", "Product Manager");




        // 데이터를 Task 객체로 생성하여 리스트에 추가
        tasks.add(new Task("프로젝트 목표와 범위 설정 후기", Arrays.asList("ProjectManager"), 4));
        tasks.add(new Task("일정과 예산 계획 수립 초기", Arrays.asList("ProjectManager", "ProductManager"), 3));
        tasks.add(new Task("일정과 예산 계획 수립 후기", Arrays.asList("ProjectManager", "ProductManager"), 3));
        tasks.add(new Task("팀 구성 및 역할 할당 초기", Arrays.asList("ProductManager"), 3));
        tasks.add(new Task("팀 구성 및 역할 할당 후기", Arrays.asList("ProductManager"), 4));
        tasks.add(new Task("프로젝트 킥오프 미팅 주최 초기", Arrays.asList("ProjectManager", "BusinessOperator"), 4));
        tasks.add(new Task("프로젝트 킥오프 미팅 주최 후기", Arrays.asList("ProjectManager", "BusinessOperator"), 5));
        tasks.add(new Task("서비스 수익 분석 초기", Arrays.asList("ProductManager"), 4));
        tasks.add(new Task("서비스 수익 분석 후기", Arrays.asList("ProductManager"), 5));
        tasks.add(new Task("시장 조사 및 경쟁 분석 초기", Arrays.asList("ProductManager"), 4));
        tasks.add(new Task("시장 조사 및 경쟁 분석 후기", Arrays.asList("BusinessOperator"), 5));
        tasks.add(new Task("사용자 요구사항 수집 및 분석 후기", Arrays.asList("ProductManager", "BusinessOperator"), 4));
        tasks.add(new Task("제품 비전과 전략 수립 초기", Arrays.asList("BusinessOperator", "ProductManager"), 4));
        tasks.add(new Task("제품 비전과 전략 수립 후기", Arrays.asList("BusinessOperator", "ProductManager"), 3));
        tasks.add(new Task("MVP 정의 초기", Arrays.asList("Developer"), 4));
        tasks.add(new Task("MVP 정의 후기", Arrays.asList("Developer"), 3));
        tasks.add(new Task("비즈니스 요구사항 분석 초기", Arrays.asList("ProductManager", "BusinessOperator"), 4));
        tasks.add(new Task("비즈니스 요구사항 분석 후기", Arrays.asList("ProductManager", "BusinessOperator"), 3));
        tasks.add(new Task("이해관계자 인터뷰 및 요구사항 문서화 초기", Arrays.asList("ProjectManager", "BusinessOperator"), 5));
        tasks.add(new Task("이해관계자 인터뷰 및 요구사항 문서화 후기", Arrays.asList("ProjectManager", "BusinessOperator"), 4));
        tasks.add(new Task("비즈니스 프로세스 정의 초기", Arrays.asList("BusinessOperator"), 4));
        tasks.add(new Task("비즈니스 프로세스 정의 후기", Arrays.asList("BusinessOperator"), 3));
        tasks.add(new Task("기술 스택 선정 초기", Arrays.asList("TechLead"), 2));
        tasks.add(new Task("기술 스택 선정 후기", Arrays.asList("TechLead"), 4));
        tasks.add(new Task("시스템 아키텍처 설계 초기", Arrays.asList("Developer", "TechLead"), 2));
        tasks.add(new Task("시스템 아키텍처 설계 후기", Arrays.asList("Developer", "TechLead"), 2));


        // List<TasksHistory> 생성 및 데이터 할당
        List<TasksHistory> tasksHistoryList = new ArrayList<>();

        tasksHistoryList.add(new TasksHistory(
                "프로젝트 목표와 범위 설정 초기",
                Arrays.asList("최프로", "이덕마", "이비례"),
                Arrays.asList("ProjectManager", "BusinessOperator"),
                3,
                "done",
                true
        ));

        tasksHistoryList.add(new TasksHistory(
                "일정과 예산 계획 수립 초기",
                Arrays.asList("이덕마"),
                Arrays.asList("ProjectManager", "ProductManager"),
                2,
                "done",
                true
        ));

        tasksHistoryList.add(new TasksHistory(
                "팀 구성 및 역할 할당 초기",
                Arrays.asList("이비례"),
                Arrays.asList("ProductManager"),
                2,
                "done",
                true
        ));

        tasksHistoryList.add(new TasksHistory(
                "서비스 수익 분석 초기",
                Arrays.asList("최덕마"),
                Arrays.asList("BusinessOperator"),
                3,
                "done",
                true
        ));

        tasksHistoryList.add(new TasksHistory(
                "시장 조사 및 경쟁 분석 초기",
                Arrays.asList("최덕마"),
                Arrays.asList("BusinessOperator"),
                3,
                "done",
                true
        ));

        tasksHistoryList.add(new TasksHistory(
                "제품 비전과 전략 수립 초기",
                Arrays.asList("최덕마", "최비례"),
                Arrays.asList("BusinessOperator", "ProductManager"),
                3,
                "done",
                true
        ));

        tasksHistoryList.add(new TasksHistory(
                "비즈니스 요구사항 분석 초기",
                Arrays.asList("이덕마", "이비례"),
                Arrays.asList("ProductManager", "BusinessOperator"),
                3,
                "progress",
                true
        ));

        tasksHistoryList.add(new TasksHistory(
                "MVP 정의 초기",
                Arrays.asList("유백데"),
                Arrays.asList("Developer"),
                0,
                "not started",
                false
        ));

        tasksHistoryList.add(new TasksHistory(
                "이해관계자 인터뷰 및 요구사항 문서화 초기",
                Arrays.asList("이프로", "이비례"),
                Arrays.asList("ProjectManager", "BusinessOperator"),
                0,
                "not started",
                false
        ));

        // 작업 추가 (작업 이름 예시)
        List<String> selectedTasknames = Arrays.asList("프로젝트 목표와 범위 설정", "일정과 예산 계획 수립", "팀 구성 및 역할 할당");

        List<Task> selectedTasks = new ArrayList<>();
        selectedTasks = taskAssigner(tasks, selectedTasknames, tasksHistoryList);
        ///여기까지 작업할당 완료
        ///여기까지 작업할당 완료


        System.out.println("\nsorted done ? ");
        for (int i = 0; i < selectedTasks.size(); i++) {

            System.out.println(selectedTasks.get(i).getName() + " difficulty : " + selectedTasks.get(i).getDifficulty());
        }

        System.out.println("Task Completion Times: " + 1);

        // 팀 멤버와 작업 정의
        List<TeamMember> selectedMembers = selectMember(selectedTasks, members, false);


        // 팀 멤버 출력
        for (int i =0; i < selectedMembers.size(); i++) {
            TeamMember member = selectedMembers.get(i);
            System.out.println(member.getName());

            Map<String, Integer> evaluations = member.getEvaluations();
            hashMapSplitter(evaluations);
        }

        List<String> selectedTaskNames = new ArrayList<>();

// selectedTasks에서 task 이름을 리스트에 추가
        for (int i = 0; i < selectedTasks.size(); i++) {
            selectedTaskNames.add(selectedTasks.get(i).getName());
            System.out.println(selectedTasks.get(i).getName());
        }

// 리스트를 String 배열로 변환
        String[] taskNamesArray = selectedTaskNames.toArray(new String[0]);

        double[][] timeMatrixx = generateTimeMatrix(selectedTasks, selectedMembers);

        System.out.println("\nStart optimization");
        for (int i = 0; i < timeMatrixx.length; i++) {
            for (int j = 0; j < timeMatrixx[i].length; j++) {
                System.out.print(timeMatrixx[i][j] + "\t");
            }
            System.out.println();  // 행이 끝날 때 줄바꿈
        }

        for (int i = 0; i< selectedTasks.size(); i++){
            System.out.println("\nCase " + (i+1) + ": "+ selectedTasks.get(i).getName() + " - 작업 최적화 먼저");
            findOptimalTeamCombination2(selectedMembers, timeMatrixx, selectedTaskNames, i);
        }



        // 각 팀 멤버가 작업을 완료하는 데 걸리는 시간 (불가능한 경우 Double.POSITIVE_INFINITY)
        double[][] timeMatrix = {
                {6, Double.POSITIVE_INFINITY, 6},  // 팀 멤버 a
                {7, 6, Double.POSITIVE_INFINITY},  // 팀 멤버 b
                {6, 5, 7},                        // 팀 멤버 c
                {8, Double.POSITIVE_INFINITY, 5},  // 팀 멤버 d
                {7, 8, 6},                        // 팀 멤버 e
                {Double.POSITIVE_INFINITY, 7, 4}   // 팀 멤버 f
        };

        System.out.println("\nStart optimization");
        for (int i = 0; i < timeMatrix.length; i++) {
            for (int j = 0; j < timeMatrix[i].length; j++) {
                System.out.print(timeMatrix[i][j] + "\t");
            }
            System.out.println();  // 행이 끝날 때 줄바꿈
        }


    String[] teamMembers = {"a", "b", "c", "d", "e", "f"};
        List<String> tasks_sample = Arrays.asList("Z", "X", "C");

        // 1. Z 작업을 먼저 고려한 경우
        System.out.println("Case 1: Z 작업 최적화 먼저");
        findOptimalTeamCombination(teamMembers, timeMatrix, tasks_sample, 0);

        // 2. X 작업을 먼저 고려한 경우
        System.out.println("\nCase 2: X 작업 최적화 먼저");
        findOptimalTeamCombination(teamMembers, timeMatrix, tasks_sample, 1);

        // 3. C 작업을 먼저 고려한 경우
        System.out.println("\nCase 3: C 작업 최적화 먼저");
        findOptimalTeamCombination(teamMembers, timeMatrix, tasks_sample, 2);

    }

}

