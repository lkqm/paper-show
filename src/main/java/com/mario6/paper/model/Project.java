package com.mario6.paper.model;

import lombok.Data;

import java.io.Serializable;

/**
 * 项目实体
 *
 * @author Mario Luo
 * @date 2019.01.19 10:48
 */
@Data
public class Project implements Serializable {
    private String id;
    private String name;
    private String description;
    private String creator;
    private Long createTime;
    private Long updateTime;

    /**
     * 项目入口: index.html
     */
    private String entranceUri;

    /**
     * 是否已经上传
     */
    private Boolean uploaded;
}
