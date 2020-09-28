package com.github.lkqm.paper.repository

import com.github.lkqm.paper.model.Project

/**
 * ProjectRepository
 *
 * @author Mario Luo
 * @date 2019.01.19 11:23
 */
interface ProjectRepository {
    fun findOne(id: String): Project?
    fun save(project: Project): Int
    fun findAll(): List<Project>
    fun delete(id: String?): Int
}