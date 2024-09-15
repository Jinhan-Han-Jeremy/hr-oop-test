package org.example;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TeamMemberFactory {

    public static TeamMemberInterface createTeamMember(String name, String role, int level, boolean state, List<String> skills,  Map<String, Integer> evaluations) {
        if (level < 1 || level > 3) {
            throw new IllegalArgumentException("Level must be between 1 and 3.");
        }
        return new TeamMember(name, role, level, state, skills,  evaluations);
    }

    public static List<TeamMemberInterface> createTeam(String role, int numberOfMembers) {
        List<TeamMemberInterface> team = new ArrayList<>();
        for (int i = 0; i < numberOfMembers; i++) {
            int level = (i % 3) + 1;  // 1~3 레벨로 순환
            //team.add(createTeamMember(role + " " + (i + 1), role, level, List.of("Skill A", "Skill B")));
        }
        return team;
    }
}