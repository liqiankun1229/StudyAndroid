package com.lqk.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * @author LQK
 * @time 2022/9/5 14:20
 *
 */
class CustomPlugin : Plugin<Project> {
    private var mProject: Project? = null
    override fun apply(project: Project) {
        println("==== 自定义 Plugin ====")
        mProject = project
        mProject?.gradle?.addBuildListener(BuildTimeListener())
    }
}