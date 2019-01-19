package com.mario6.paper.model;

import java.io.Serializable;
import lombok.Data;

/**
 * 项目版本实体
 *
 * @author Mario Luo
 * @date 2019.01.19 11:04
 */
@Data
public class ProjectVersion implements Serializable {

    private String id;

    private transient String projectId;

    private String description;

    private Long createTime;

    private Long updateTime;
}
