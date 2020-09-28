package com.github.lkqm.paper

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

/**
 * 主程序
 *
 * @author Mario Luo
 * @date 2019.01.19 10:22
 */
@SpringBootApplication
open class PaperMain

fun main(args: Array<String>) {
    SpringApplication.run(PaperMain::class.java, *args)
}