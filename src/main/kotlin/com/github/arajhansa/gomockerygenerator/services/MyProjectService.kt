package com.github.arajhansa.gomockerygenerator.services

import com.github.arajhansa.gomockerygenerator.MyBundle
import com.intellij.openapi.project.Project

class MyProjectService(project: Project) {

    init {
        println(MyBundle.message("projectService", project.name))
    }
}
