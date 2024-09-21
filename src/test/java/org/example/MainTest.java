package org.example;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MainTest {
// 실행은 ctrl shft f10
    @Test
    void name() {
        System.out.println("hello world"); //sout
        String abc = removeWords("fkdf 초기"); //alt shft v  // ctrl alt v
        Assertions.assertEquals("fkdf", abc);

        //ctrl p 는 파라미터 타입 체크
        boolean b = containsOrContained("jinhanhan", "hanhan");
        Assertions.assertEquals(true, b);
    }


    // 두 문자열 중 하나가 다른 하나를 포함하는지 확인하는 함수
    public static boolean containsOrContained(String selectedTaskName, String theWord) {
        // selectedTaskName이 theWord를 포함하거나 theWord가 selectedTaskName을 포함하면 true 반환
        return selectedTaskName.contains(theWord) || theWord.contains(selectedTaskName);
    }

    public String removeWords(String taskName) {
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

    @Test
    void unitest1() {
        String s = removeWords(" 초기후기");
        Assertions.assertEquals(" 초기후기", s);
        boolean b = containsOrContained("truetrue", "true");
        Assertions.assertEquals(true,b);
    }
}