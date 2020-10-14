package com.github.lkqm.paper.controller

import com.github.lkqm.paper.model.Project
import com.github.lkqm.paper.service.ProjectService
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

import javax.annotation.Resource

/**
 * 项目控制器
 *
 * @author Mario Luo
 * @date 2019.01.19 10:46
 */
@RestController
@RequestMapping("/api/projects")
class ProjectController {

    @Resource
    private ProjectService projectService

    @PostMapping
    String addProject(Project project) {
        return projectService.saveProject(project)
    }

    @GetMapping
    List<Project> listProject() {
        return projectService.getProjects()
    }

    @GetMapping("/{id}")
    Project getProject(@PathVariable String id) {
        return projectService.getProject(id)
    }

    @PostMapping("/{id}/upload")
    void upload(@PathVariable String id, @RequestParam(required = false, defaultValue = "1") Integer type, String linkUrl, String entranceUri, MultipartFile file) {
        Project project = new Project()
        project.setId(id)
        project.setEntranceUri(entranceUri)
        project.setType(type)
        project.setLinkUrl(linkUrl)
        projectService.saveProjectFile(project, file)
    }
}
