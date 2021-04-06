package com.github.arajhansa.gomockerygenerator.listener

import com.intellij.execution.process.ProcessEvent
import com.intellij.execution.process.ProcessListener
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.guessProjectDir
import com.intellij.openapi.util.Key
import com.intellij.openapi.vfs.VfsUtil

class GenerationProcessListener(private val project: Project) : ProcessListener {

    override fun startNotified(event: ProcessEvent) {
        println(event)
    }

    override fun processTerminated(event: ProcessEvent) {
        println("Process finished with exit code " + event.exitCode)
        VfsUtil.markDirtyAndRefresh(false, true, true, project.guessProjectDir())
    }

    override fun onTextAvailable(event: ProcessEvent, outputType: Key<*>) {}
}