package com.github.lkqm.paper.model
/**
 * 项目实体
 *
 * @author Mario Luo
 * @date 2019.01.19 10:48
 */
class Project implements Serializable {

    /**
     * 项目Id
     */
    String id

    /**
     * 项目名称
     */
    String name

    /**
     * 项目描述
     */
    String description

    /**
     * 项目创建人
     */
    String creator

    /**
     * 项目创建时间
     */
    Long createTime

    /**
     * 项目更新时间
     */
    Long updateTime

    /**
     * 类型, 1: 上传文件, 2: 外部地址
     */
    Integer type = 1

    /**
     * 项目入口: index.html
     */
    String entranceUri

    /**
     * 是否已经上传
     */
    Boolean uploaded

    /**
     * 外部跳转地址
     */
    String linkUrl
}
