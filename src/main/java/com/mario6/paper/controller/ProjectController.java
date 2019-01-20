package com.mario6.paper.controller;

import com.mario6.paper.model.Project;
import com.mario6.paper.service.ProjectService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
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

    // 添加项目
    @PostMapping
    public String add(Project project) {
        return projectService.saveProject(project);
    }

    // 获取项目列表
    @GetMapping
    public List<Project> getProjects() {
        return projectService.getProjects();
    }

    // 获得单个项目
    @GetMapping("/{id}")
    public Project getProject(@PathVariable String id) {
        return projectService.getProject(id);
    }

    // 上传某个项目的版本
    @PostMapping("/{id}/upload")
    public void upload(@PathVariable String id,
                       String entranceUri,
                       @RequestParam("file") MultipartFile file) {
        Project project = new Project();
        project.setId(id);
        project.setEntranceUri(entranceUri);
        projectService.saveProjectFile(project, file);
    }

    // 下载项目
    @GetMapping("/{id}/download")
    public File download(@PathVariable String id) {
        String dir = projectService.getProjectFilePath(id);
        return new File(dir);
    }
}
