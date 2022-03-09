package com.example.lib

import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * 自定义插件
 */
class CustomPlugin: Plugin<Project> {
    override fun apply(project: Project) {
        println("自定义插件")
    }
}