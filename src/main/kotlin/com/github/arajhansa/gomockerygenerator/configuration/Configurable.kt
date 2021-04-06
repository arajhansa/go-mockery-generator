package com.github.arajhansa.gomockerygenerator.configuration

import com.github.arajhansa.gomockerygenerator.view.SettingsUI
import com.intellij.openapi.options.ConfigurableBase

class Configurable : ConfigurableBase<SettingsUI, Configs>(
    "go.mockeryGenerator",
    "Go Mockery Generator",
    null
) {

    override fun getSettings(): Configs {
        return Configs.instance
    }

    override fun createUi(): SettingsUI {
        return SettingsUI()
    }

}
