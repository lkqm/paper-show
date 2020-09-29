package com.github.lkqm.paper.model


/**
 * 项目包装
 */
data class ProjectDataWrapper (
    var projects: MutableList<Project>? = null
)

/**
 * 项目实体
 */
data class Project (

    /** 项目Id */
    var id: String? = null,

    /** 项目名称 */
    var name: String? = null,

    /** 项目描述 */
    var description: String? = null,

    /** 项目创建人 */
    var creator: String? = null,

    /** 项目创建时间 */
    var createTime: Long? = null,

    /** 项目更新时间 */
    var updateTime: Long? = null,

    /** 类型, 1: 上传文件, 2: 外部地址 */
    var type: Int? = 1,

    /** 项目入口: index.html */
    var entranceUri: String? = null,

    /** 是否已经上传 */
    var uploaded: Boolean? = null,

    /** 外部跳转地址 */
    var linkUrl: String? = null
)

/**
 * 项目类型
 */
enum class ProjectType(val code: Int, val message: String) {

    FILE(1, "文件"),
    LINK(2, "链接");

    companion object {
        fun of(code: Int): ProjectType? {
            for (one in values()) {
                if (one.code == code) {
                    return one
                }
            }
            return null
        }
    }
}