package com.mario6.paper.model;

import java.io.Serializable;
import java.util.List;
import lombok.Data;

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
    private List<ProjectVersion> versions;
}
