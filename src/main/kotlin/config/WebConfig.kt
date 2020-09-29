package com.github.lkqm.paper.config

import com.github.lkqm.paper.util.FileUtils
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter
import java.io.File
import javax.annotation.Resource

/**
 * WEB配置：自定义静态资源目录
 */
@Configuration
open class WebConfig : WebMvcConfigurerAdapter() {

    @Resource
    lateinit var properties: PaperProperties

    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        FileUtils.makeParent(properties.workDir)
        val dir = File(properties.workDir)
        val location = "file:" + dir.absolutePath + File.separator + PaperProperties.PROJECTS_DIR_NAME + File.separator
        registry.addResourceHandler("/v/**").addResourceLocations(location)
        super.addResourceHandlers(registry)
    }
}