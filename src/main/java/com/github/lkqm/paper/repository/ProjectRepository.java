package com.github.lkqm.paper.repository;

import com.github.lkqm.paper.model.Project;

import java.util.List;

/**
 * ProjectRepository
 *
 * @author Mario Luo
 * @date 2019.01.19 11:23
 */
public interface ProjectRepository {

    Project findOne(String id);

    int save(Project project);

    List<Project> findAll();

    int delete(String id);
}
