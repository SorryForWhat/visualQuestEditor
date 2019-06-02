package com.example.demo.app

import javafx.scene.text.FontWeight
import tornadofx.Stylesheet
import tornadofx.box
import tornadofx.cssclass
import tornadofx.px
import tornadofx.*

class Styles : Stylesheet() {
    companion object {
        val heading by cssclass()
    }

    init {
        label and heading {
            padding = box(400.px)
            fontSize = 50.px
            fontWeight = FontWeight.BOLD
            backgroundColor += c("#cecece")
        }
    }
}