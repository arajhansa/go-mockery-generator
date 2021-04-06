package com.github.arajhansa.gomockerygenerator.configuration

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.State
import com.intellij.util.xmlb.XmlSerializerUtil
import com.intellij.util.xmlb.annotations.Tag

@State(name = "GoMockeryGenerator")
@Service
class Configs : PersistentStateComponent<Configs.GoGenerateMockeryConfigsState> {

    companion object {
        val instance = Configs()
    }

    data class GoGenerateMockeryConfigsState(
        @Tag("exec-path") var executablePath: String? = ""
    )

    val currentState = GoGenerateMockeryConfigsState()

    override fun getState(): GoGenerateMockeryConfigsState = currentState

    override fun loadState(state: GoGenerateMockeryConfigsState) = XmlSerializerUtil.copyBean(state, currentState)

}