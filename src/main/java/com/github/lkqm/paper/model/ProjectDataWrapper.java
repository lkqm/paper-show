package com.github.lkqm.paper.model;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * ProjectJsonWrapper
 *
 * @author Mario Luo
 * @date 2019.01.19 13:48
 */
@Data
public class ProjectDataWrapper implements Serializable {

    private List<Project> projects;

}
