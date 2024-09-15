package org.example;
import java.util.List;
import java.util.Map;

public interface TeamMemberInterface {
    // 팀원의 이름을 반환하는 메서드
    String getName();

    // 팀원의 역할을 반환하는 메서드
    String getRole();

    // 팀원의 레벨을 반환하는 메서드
    int getLevel();

    boolean getState();

    // 팀원의 스킬 리스트를 반환하는 메서드
    List<String> getSkills();

    // 특정 평가 점수를 반환하는 메서드
    Integer getEvaluation(String evaluationType);

    // 특정 평가 점수를 설정하는 메서드
    void setEvaluation(String evaluationType, int score);

    // 모든 평가를 반환하는 메서드
    Map<String, Integer> getEvaluations();

    float getPerformanceRate(List<Integer> performamceScores);

    // 객체 정보를 문자열로 반환하는 메서드 (toString 메서드는 Object 클래스에서 상속받아 이미 모든 클래스가 가지고 있지만, 인터페이스에 명시적으로 선언할 수 있음)
    String toString();
}