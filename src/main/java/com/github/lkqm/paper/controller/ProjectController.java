package com.github.lkqm.paper.controller;

import com.github.lkqm.paper.model.Project;
import com.github.lkqm.paper.service.ProjectService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.List;

/**
 * 项目控制器
 *
 * @author Mario Luo
 * @date 2019.01.19 10:46
 */
@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    @Resource
    private ProjectService projectService;

    @PostMapping
    public String addProject(Project project) {
        return projectService.saveProject(project);
    }

    @GetMapping
    public List<Project> listProject() {
        return projectService.getProjects();
    }

    @GetMapping("/{id}")
    public Project getProject(@PathVariable String id) {
        return projectService.getProject(id);
    }

    @PostMapping("/{id}/upload")
    public void upload(@PathVariable String id, String entranceUri, @RequestPart("file") MultipartFile file) {
        Project project = new Project();
        project.setId(id);
        project.setEntranceUri(entranceUri);
        projectService.saveProjectFile(project, file);
    }
}
