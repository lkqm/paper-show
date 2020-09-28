package com.github.lkqm.paper.service

import com.github.lkqm.paper.config.PaperProperties
import com.github.lkqm.paper.model.Project
import com.github.lkqm.paper.model.ProjectType
import com.github.lkqm.paper.repository.ProjectRepository
import com.github.lkqm.paper.util.ArchiveUtilsExt
import com.github.lkqm.paper.util.AssertUtils.checkParam
import com.github.lkqm.paper.util.AssertUtils.checkParamNotNull
import com.github.lkqm.paper.util.FileUtils
import org.apache.commons.lang3.RandomStringUtils
import org.apache.commons.lang3.StringUtils
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.io.IOException
import javax.annotation.Resource

/**
 * 项目相关服务
 *
 * @author Mario Luo
 * @date 2019.01.19 10:51
 */
@Service
class ProjectService {
    @Resource
    lateinit var config: PaperProperties

    @Resource
    lateinit var repository: ProjectRepository

    /**
     * 添加项目
     */
    fun saveProject(project: Project): String {
        checkSaveProjectParams(project)
        var id = project.id
        if (StringUtils.isBlank(id)) {
            id = RandomStringUtils.random(8, "ABCDEFGHIJQMNOPQRSTUVWXYZ0123456789")
            project.id = id
        }
        val time = System.currentTimeMillis()
        project.createTime = time
        project.updateTime = time
        project.uploaded = false
        project.entranceUri = null
        repository.save(project)
        return id!!
    }

    private fun checkSaveProjectParams(project: Project) {
        val id = StringUtils.trim(project.id)
        if (StringUtils.isNotEmpty(id)) {
            val p = repository.findOne(id)
            checkParam(p == null, "ID已存在")
            checkParam(id.matches(Regex("^[A-Za-z0-9\\-_]*$")), "ID有效字符是字母或数字")
        }
        val name = StringUtils.trim(project.name)
        val nameSize = StringUtils.length(name)
        checkParam(nameSize >= 2 && nameSize <= 12, "项目名称长度无效, 长度2~12字符")
        val creator = StringUtils.trim(project.creator)
        val creatorSize = StringUtils.length(creator)
        checkParam(creatorSize >= 2 && creatorSize <= 24, "创建人长度无效, 长度2~24字符")
        val description = StringUtils.trim(project.description)
        val descSize = StringUtils.length(description)
        checkParam(descSize <= 512, "描述超出限定512个字符")

        // 重设参数
        project.id = id
        project.name = name
        project.creator = creator
        project.description = description
    }

    /**
     * 获得所有项目
     */
    fun getProjects(): List<Project> {
        return repository.findAll()
    }

    /**
     * 获得单个项目
     */
    fun getProject(id: String): Project? {
        return repository.findOne(id)
    }

    /**
     * 保存一个一个项目版本
     */
    fun saveProjectFile(params: Project, file: MultipartFile) {
        val project = checkSaveProjectFileParams(params, file)
        val type: ProjectType? = params.type?.let { ProjectType.of(it) }
        if (type == ProjectType.FILE) {
            // 存储文件
            val workDir = config.workDir
            val absWorkDir = File(workDir).absolutePath
            val targetFilePath = absWorkDir + File.separator + PaperProperties.Companion.UPLOAD_DIR_NAME + File.separator + project.id + '-' + System.currentTimeMillis() + "-" + file.originalFilename
            FileUtils.makeParent(targetFilePath)
            try {
                file.transferTo(File(targetFilePath))
                // 解压到指定目录
                val projectPath = absWorkDir + File.separator + PaperProperties.Companion.PROJECTS_DIR_NAME + File.separator + project.id + File.separator
                FileUtils.emptyDir(projectPath)
                ArchiveUtilsExt.unZip(targetFilePath, projectPath)
            } catch (e: IOException) {
                throw RuntimeException(e)
            }
        } else if (type == ProjectType.LINK) {
            project.linkUrl = params.linkUrl
        }
        project.type = params.type
        project.uploaded = true
        project.entranceUri = params.entranceUri
        project.updateTime = System.currentTimeMillis()
        repository.save(project)
    }

    private fun checkSaveProjectFileParams(params: Project, file: MultipartFile): Project {
        params.entranceUri = StringUtils.trim(params.entranceUri)
        params.linkUrl = StringUtils.trim(params.linkUrl)
        val id = StringUtils.trim(params.id)
        checkParam(StringUtils.isNotEmpty(id), "参数projectId不能为空")
        val project = repository.findOne(id)
        checkParamNotNull(project, "项目不存在")
        val type: ProjectType? = params.type?.let { ProjectType.of(it) }
        checkParamNotNull(type, "参数type无效")
        if (type == ProjectType.FILE) {
            val entrance = params.entranceUri
            if (StringUtils.isNotEmpty(entrance)) {
                checkParam(entrance!!.matches(Regex("^[A-Za-z0-9\\-_\\.]*$")), "入口地址, 只支持字母,数字,下划线, 小数点和中划线")
            }
            checkParamNotNull(file, "上传文件不能为空")
            checkParam(!file.isEmpty, "上传文件不能为空")
        } else if (type == ProjectType.LINK) {
            checkParam(StringUtils.isNotEmpty(params.linkUrl), "跳转链接不能为空")
        }
        return project!!
    }

    /**
     * 获取存储文件目录
     *
     * @param id
     * @return
     */
    fun getProjectFilePath(id: String): String {
        val workDir = config.workDir
        val absWorkDir = File(workDir).absolutePath
        return absWorkDir + File.separator + PaperProperties.PROJECTS_DIR_NAME + File.separator + id
    }
}