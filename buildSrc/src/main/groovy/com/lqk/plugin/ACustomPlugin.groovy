//package com.lqk.plugin
//
//import org.gradle.api.Plugin
//import org.gradle.api.Project
//
//class ACustomPlugin implements Plugin<Project> {
//    private Project mProject;
//
//    @Override
//    void apply(Project project) {
//        println("自定义插件")
//        mProject = project;
//        project.gradle.addListener(new BuildTimeListener())
//    }
//}