package com.github.lkqm.paper.model;

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

    /**
     * 项目Id
     */
    private String id;

    /**
     * 项目名称
     */
    private String name;

    /**
     * 项目描述
     */
    private String description;

    /**
     * 项目创建人
     */
    private String creator;

    /**
     * 项目创建时间
     */
    private Long createTime;

    /**
     * 项目更新时间
     */
    private Long updateTime;

    /**
     * 类型, 1: 上传文件, 2: 外部地址
     */
    private Integer type = 1;

    /**
     * 项目入口: index.html
     */
    private String entranceUri;

    /**
     * 是否已经上传
     */
    private Boolean uploaded;

    /**
     * 外部跳转地址
     */
    private String linkUrl;
}
