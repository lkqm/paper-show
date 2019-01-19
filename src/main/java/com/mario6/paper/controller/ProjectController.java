package com.mario6.paper.controller;

import com.mario6.paper.model.Project;
import com.mario6.paper.model.ProjectVersion;
import com.mario6.paper.service.ProjectService;
import java.io.File;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
    @PostMapping("/{id}/versions")
    public void saveVersions(@PathVariable String id, ProjectVersion projectVersion, @RequestParam("file") MultipartFile file) {
        projectVersion.setProjectId(id);
        projectService.saveProjectVersion(projectVersion, file);
    }

    // 下载项目
    @GetMapping("/{id}/download")
    public File download(@PathVariable String id, @RequestParam(name = "v", required = false) String version) {
        String dir = projectService.getProjectFilePath(id, version);
        return new File(dir);
    }
}
