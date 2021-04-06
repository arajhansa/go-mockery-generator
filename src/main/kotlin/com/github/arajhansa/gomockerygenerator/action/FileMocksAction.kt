package com.github.arajhansa.gomockerygenerator.action

import com.github.arajhansa.gomockerygenerator.configuration.Configs
import com.github.arajhansa.gomockerygenerator.exception.NotifyException
import com.github.arajhansa.gomockerygenerator.exception.NotifyInfo
import com.github.arajhansa.gomockerygenerator.listener.GenerationProcessListener
import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.execution.process.ProcessHandlerFactory
import com.intellij.execution.process.ProcessTerminatedListener
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.actionSystem.LangDataKeys
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.ui.popup.JBPopupFactory
import com.intellij.psi.PsiFile
import java.nio.charset.Charset
import java.util.regex.Pattern


class FileMocksAction : AnAction() {

    override fun actionPerformed(e: AnActionEvent) {
        if (e.project == null) throw NotifyException("Cannot read project")
        if (e.getData(LangDataKeys.EDITOR) == null) throw NotifyException("Cannot read file")

        val document = e.getData(LangDataKeys.EDITOR)!!.document

        val interfaces = document.text.lines().mapNotNull {
            val matcher = Pattern.compile("type (.*?) interface").matcher(it)
            if (matcher.find()) matcher.group(1) else null
        }

        if (interfaces.isEmpty()) throw NotifyInfo("Cannot find golang interfaces to mock")

        val popup = JBPopupFactory.getInstance().createPopupChooserBuilder(interfaces)
        popup.setItemChosenCallback { generateMock(e, it) }
        popup.createPopup().showInBestPositionFor(e.dataContext)
    }

    override fun update(e: AnActionEvent) {
        val document = e.getData(LangDataKeys.EDITOR)!!.document
        val extension = FileDocumentManager.getInstance().getFile(document)!!.extension
        if (extension.isNullOrBlank() || extension.toLowerCase() != "go")
            e.presentation.isEnabledAndVisible = false
    }

    private fun generateMock(e: AnActionEvent, selectedInterface: String) {
        apply(e, "--name", selectedInterface)
    }

    fun apply(actionEvent: AnActionEvent, paramName: String, vararg vals: String) {
        val cmd = GeneralCommandLine()
        cmd.charset = Charset.forName("UTF-8")

        val psiFile: PsiFile = actionEvent.getData(CommonDataKeys.PSI_FILE)!!
        cmd.setWorkDirectory(psiFile.virtualFile.parent.canonicalPath)

        val execPath = Configs.instance.currentState.executablePath
        if (execPath.isNullOrBlank()) throw NotifyInfo("Please set Mockery Executable Path in settings")
        cmd.exePath = execPath

        cmd.addParameters(paramName, *vals)

        try {
            val handler = ProcessHandlerFactory.getInstance().createProcessHandler(cmd)
            handler.addProcessListener(GenerationProcessListener(actionEvent.project!!))
            ProcessTerminatedListener.attach(handler, actionEvent.project)
            handler.startNotify()
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }
}