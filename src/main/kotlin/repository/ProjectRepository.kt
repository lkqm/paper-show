package com.github.lkqm.paper.repository

import com.github.lkqm.paper.model.Project

/**
 * 项目持久层
 */
interface ProjectRepository {

    fun findOne(id: String): Project?

    fun save(project: Project): Int

    fun findAll(): List<Project>

    fun delete(id: String): Int

}