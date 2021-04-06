package com.github.arajhansa.gomockerygenerator.view

import com.github.arajhansa.gomockerygenerator.configuration.Configs
import com.intellij.openapi.Disposable
import com.intellij.openapi.fileChooser.FileChooser
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory
import com.intellij.openapi.options.ConfigurableUi
import com.intellij.openapi.ui.ComponentWithBrowseButton
import com.intellij.openapi.util.io.FileUtil
import com.intellij.openapi.vfs.VirtualFileManager
import com.intellij.ui.components.JBTextField
import com.intellij.util.ui.FormBuilder
import com.intellij.util.ui.UI.PanelFactory
import java.awt.BorderLayout
import javax.swing.JComponent
import javax.swing.JPanel

class SettingsUI : ConfigurableUi<Configs>, Disposable {

    private var executablePathField = ExecutablePathField()

    override fun reset(settings: Configs) {
        executablePathField.setPath(settings.currentState.executablePath)
    }

    override fun isModified(settings: Configs): Boolean {
        return FileUtil.comparePaths(executablePathField.getPath(), settings.currentState.executablePath) != 0
    }

    override fun apply(settings: Configs) {
        if (executablePathField.getPath().isNullOrBlank()) return

        settings.currentState.executablePath = executablePathField.getPath()
        Configs.instance.currentState.executablePath = executablePathField.getPath()
    }

    override fun getComponent(): JComponent {
        val currentPath = Configs.instance.currentState.executablePath
        executablePathField =
            if (!currentPath.isNullOrBlank()) ExecutablePathField(currentPath) else ExecutablePathField()
        val panelBuilder = PanelFactory.panel(executablePathField).withLabel("Mockery Path :")
        val form = FormBuilder.createFormBuilder().addComponent(panelBuilder.createPanel()).panel

        val panel = JPanel(BorderLayout())
        panel.add(form, "North")

        return panel
    }

    override fun dispose() {
        executablePathField.dispose()
    }

    class ExecutablePathField(initPath: String? = "<Empty>") : ComponentWithBrowseButton<JBTextField>(
        JBTextField(),
        null
    ), Disposable {
        private val fileChooserDescriptor = FileChooserDescriptorFactory.createSingleFileNoJarsDescriptor()

        init {
            childComponent.isEditable = false
            childComponent.text = initPath

            fileChooserDescriptor.title = "Select Mockery Executable Path"

            addActionListener { showOptions() }
        }

        private fun showOptions() {
            val selectedDir = VirtualFileManager.getInstance().findFileByUrl("")
            FileChooser.chooseFile(fileChooserDescriptor, null, this, selectedDir) {
                childComponent.text = it.path
            }
        }

        fun getPath(): String? {
            return childComponent.text
        }

        fun setPath(path: String?) {
            childComponent.text = path ?: "<Empty>"
        }
    }

}