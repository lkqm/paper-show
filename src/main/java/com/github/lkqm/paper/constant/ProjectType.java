package com.github.lkqm.paper.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ProjectType {

    FILE(1, "文件"),
    LINK(2, "链接");

    private Integer code;
    private String message;

    public static ProjectType of(Integer code) {
        for (ProjectType one : ProjectType.values()) {
            if (one.code.equals(code)) {
                return one;
            }
        }
        return null;
    }
}
