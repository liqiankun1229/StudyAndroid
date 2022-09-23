package com.lqk.plugin

import org.gradle.BuildListener
import org.gradle.BuildResult
import org.gradle.api.Task
import org.gradle.api.execution.TaskExecutionListener
import org.gradle.api.initialization.Settings
import org.gradle.api.invocation.Gradle
import org.gradle.api.tasks.TaskState

/**
 * @author LQK
 * @time 2022/9/5 11:30
 *
 */
class BuildTimeListener : TaskExecutionListener, BuildListener {
    private var taskTimeMap = linkedMapOf<String, TaskTimeInfo>()

    inner class TaskTimeInfo {

        var total: Long = 0L
        var path: String = ""
        var start: Long = 0L
        var end: Long = 0L
    }

    /**
     * 开始执行任务之前
     */
    override fun beforeExecute(task: Task) {
        val taskTimeInfo = TaskTimeInfo()
        taskTimeInfo.start = System.currentTimeMillis()
        taskTimeInfo.path = task.path
        taskTimeMap[task.path] = taskTimeInfo
    }

    /**
     * 任务执行完成之后
     */
    override fun afterExecute(task: Task, taskState: TaskState) {
        val taskTimeInfo = taskTimeMap[task.path]
        taskTimeInfo?.end = System.currentTimeMillis()
        taskTimeInfo?.let {
            it.total = it.end - it.start
        }
    }

    override fun settingsEvaluated(settings: Settings) {

    }

    override fun projectsLoaded(gradle: Gradle) {

    }

    override fun projectsEvaluated(gradle: Gradle) {

    }

    override fun buildFinished(buildResult: BuildResult) {
        println("==============================")
        println("")
        for (mutableEntry in taskTimeMap) {
            println("${mutableEntry.value.path} : ${mutableEntry.value.total}")
        }
        println("")
        println("==============================")
    }
}