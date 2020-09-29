package com.github.lkqm.paper

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

/**
 * 主程序
 */
@SpringBootApplication
open class PaperMain

fun main(args: Array<String>) {
    SpringApplication.run(PaperMain::class.java, *args)
}