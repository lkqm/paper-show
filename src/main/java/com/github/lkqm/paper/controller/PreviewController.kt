package com.github.lkqm.paper.controller

import com.github.lkqm.paper.service.ProjectService
import org.apache.commons.lang3.StringUtils
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import javax.annotation.Resource

@Controller
class PreviewController {

    @Resource
    private lateinit var projectService: ProjectService

    @RequestMapping("/v/{id}/")
    fun projectIndex(@PathVariable id: String): String {
        val project = projectService.getProject(id)
        var entrance = "index.html"
        if (project != null && StringUtils.isNotBlank(project.entranceUri)) {
            entrance = StringUtils.trim(project.entranceUri)
        }
        return "/v/$id/$entrance"
    }
}