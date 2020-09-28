package com.github.lkqm.paper.config

import com.github.lkqm.paper.util.FileUtils
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter
import java.io.File
import javax.annotation.Resource

@Configuration
open class WebConfig : WebMvcConfigurerAdapter() {

    @Resource
    var properties: PaperProperties? = null

    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        val workDir = properties?.workDir
        FileUtils.makeParent(workDir)
        val dir = File(workDir)
        val path = dir.absolutePath
        val url = "/v/**"
        val location = "file:" + path + File.separator + PaperProperties.PROJECTS_DIR_NAME + File.separator
        registry.addResourceHandler(url).addResourceLocations(location)
        super.addResourceHandlers(registry)
    }
}