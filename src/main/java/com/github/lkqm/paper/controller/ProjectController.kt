package com.github.lkqm.paper.controller

import com.github.lkqm.paper.model.Project
import com.github.lkqm.paper.service.ProjectService
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import javax.annotation.Resource

/**
 * 项目控制层
 */
@RestController
@RequestMapping("/api/projects")
class ProjectController {

    @Resource
    private lateinit var projectService: ProjectService

    @PostMapping
    fun addProject(project: Project): String {
        return projectService.saveProject(project)
    }

    @GetMapping
    fun listProject(): List<Project> {
        return projectService.getProjects()
    }

    @GetMapping("/{id}")
    fun getProject(@PathVariable id: String): Project? {
        return projectService.getProject(id)
    }

    @PostMapping("/{id}/upload")
    fun upload(@PathVariable id: String, @RequestParam(required = false, defaultValue = "1") type: Int,
               linkUrl: String?, entranceUri: String?, file: MultipartFile) {
        val project = Project()
        project.id = id
        project.entranceUri = entranceUri
        project.type = type
        project.linkUrl = linkUrl
        projectService.saveProjectFile(project, file)
    }
}