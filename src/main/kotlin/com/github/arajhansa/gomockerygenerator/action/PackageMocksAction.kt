package com.github.arajhansa.gomockerygenerator.action

import com.github.arajhansa.gomockerygenerator.configuration.Configs
import com.github.arajhansa.gomockerygenerator.exception.NotifyException
import com.github.arajhansa.gomockerygenerator.exception.NotifyInfo
import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.actionSystem.LangDataKeys
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.psi.PsiFile
import java.nio.charset.Charset


class PackageMocksAction : AnAction() {

    override fun actionPerformed(e: AnActionEvent) {
        if (e.project == null) throw NotifyException("Cannot read project")
        apply(e, "--all")
    }

    override fun update(e: AnActionEvent) {
        val document = e.getData(LangDataKeys.EDITOR)!!.document
        val extension = FileDocumentManager.getInstance().getFile(document)!!.extension
        if (extension.isNullOrBlank() || extension.toLowerCase() != "go")
            e.presentation.isEnabledAndVisible = false
    }

    fun apply(actionEvent: AnActionEvent, paramName: String, vararg values: String) {
        val cmd = GeneralCommandLine()
        cmd.charset = Charset.forName("UTF-8")

        val psiFile: PsiFile = actionEvent.getData(CommonDataKeys.PSI_FILE)!!
        cmd.setWorkDirectory(psiFile.virtualFile.parent.canonicalPath)

        val execPath = Configs.instance.currentState.executablePath
        if (execPath.isNullOrBlank()) throw NotifyInfo("Please set Mockery Executable Path in settings")
        cmd.exePath = execPath

        cmd.addParameters(paramName, *values)

        try {

        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

}