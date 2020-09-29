package com.github.lkqm.paper.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component
import java.io.File

/**
 * 应用配置类
 */
@Component
@ConfigurationProperties(prefix = PaperProperties.PREFIX)
open class PaperProperties {

    /** 数据存储目录  */
    var workDir = System.getProperty("user.home") + File.separator + ".paper-show"

    companion object {
        const val PREFIX = "paper-show"
        const val PROJECTS_DIR_NAME = "projects"
        const val UPLOAD_DIR_NAME = "upload"
        const val PROJECT_META_FILE_NAME = "project.json"
    }
}