package com.github.lkqm.paper.controller

import com.github.lkqm.paper.service.ProjectService
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
        var entrance =  project?.entranceUri
        if(entrance.isNullOrEmpty()) {
            entrance = "index.html"
        }
        return "/v/$id/$entrance"
    }
}