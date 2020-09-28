package com.github.lkqm.paper.repository

import com.alibaba.fastjson.JSONObject
import com.alibaba.fastjson.JSONReader
import com.alibaba.fastjson.serializer.SerializerFeature
import com.github.lkqm.paper.config.PaperProperties
import com.github.lkqm.paper.model.Project
import com.github.lkqm.paper.model.ProjectDataWrapper
import org.apache.commons.lang3.StringUtils
import org.springframework.stereotype.Component
import java.io.*
import java.util.*
import javax.annotation.Resource

/**
 * JsonFileProjectRepositoryImpl
 *
 * @author Mario Luo
 * @date 2019.01.19 11:53
 */
@Component
class JsonFileProjectRepositoryImpl : ProjectRepository {

    @Resource
    private lateinit var configuration: PaperProperties

    override fun findOne(id: String): Project? {
        val data = readData()
        return doGetProjectById(data, id)
    }

    override fun save(project: Project): Int {
        val data = readData()
        val projects = data.projects
        val orgProject = doGetProjectById(data, project.id)
        if (orgProject != null) {
            orgProject.updateTime = project.updateTime
            orgProject.creator = project.creator
            orgProject.description = project.description
            orgProject.name = project.name
            orgProject.uploaded = project.uploaded
            orgProject.entranceUri = project.entranceUri
            orgProject.type = project.type
            orgProject.linkUrl = project.linkUrl
        } else {
            projects?.add(0, project)
        }
        writeData(data)
        return 1
    }

    override fun findAll(): List<Project> {
        val data = readData()
        if(data.projects != null) {
            return data.projects!!
        }
        return listOf()
    }

    override fun delete(id: String?): Int {
        val data = readData()
        val projects = data.projects
        var count = 0
        if(projects != null) {
            val it = projects.iterator()
            while (it.hasNext()) {
                val one = it.next()
                val pid = one.id
                if (StringUtils.equals(id, pid)) {
                    it.remove()
                    count++
                }
            }
        }
        return count
    }

    private fun doGetProjectById(data: ProjectDataWrapper, id: String?): Project? {
        val projects = data.projects
        if(projects != null) {
            for (one in projects) {
                if (StringUtils.equals(id, one.id)) {
                    return one
                }
            }
        }
        return null
    }

    private fun writeData(data: ProjectDataWrapper) {
        val file = projectMetaJsonFile
        doWriteJsonFile(file, data)
    }

    private fun readData(): ProjectDataWrapper {
        val file = projectMetaJsonFile
        try {
            FileReader(file).use { reader ->
                val jsonReader = JSONReader(reader)
                val data = jsonReader.readObject(ProjectDataWrapper::class.java)
                if (data.projects == null) {
                    data.projects = ArrayList()
                }
                return data
            }
        } catch (e: FileNotFoundException) {
            throw RuntimeException(e)
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
    }

    private val projectMetaJsonFile: File
        private get() {
            val workDir = configuration.workDir
            val path = workDir + File.separator + PaperProperties.PROJECT_META_FILE_NAME
            val file = File(path)
            if (!file.exists()) {
                val parentFile = file.parentFile
                if (!parentFile.exists()) {
                    parentFile.mkdirs()
                }
                val data = ProjectDataWrapper()
                data.projects = ArrayList()
                doWriteJsonFile(file, data)
            }
            return file
        }

    private fun doWriteJsonFile(file: File, data: ProjectDataWrapper) {
        try {
            FileWriter(file).use { writer ->
                JSONObject.writeJSONString(writer, data,
                        SerializerFeature.WriteNullListAsEmpty,
                        SerializerFeature.PrettyFormat,
                        SerializerFeature.SortField)
            }
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
    }
}