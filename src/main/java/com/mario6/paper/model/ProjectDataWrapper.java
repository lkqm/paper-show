package com.mario6.paper.model;

import java.io.Serializable;
import java.util.List;
import lombok.Data;

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
