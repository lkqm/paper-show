package com.mario6.paper.controller;

import com.mario6.paper.model.Project;
import com.mario6.paper.service.ProjectService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

@Controller
public class PreviewController {

    @Resource
    private ProjectService projectService;

    @RequestMapping("/v/{id}/")
    public String projectIndex(@PathVariable String id) {
        Project project = projectService.getProject(id);
        String entrance = "index.html";
        if (project != null && StringUtils.isNotBlank(project.getEntranceUri())) {
            entrance = StringUtils.trim(project.getEntranceUri());
        }
        return "/v/" + id + "/" + entrance;
    }

}
