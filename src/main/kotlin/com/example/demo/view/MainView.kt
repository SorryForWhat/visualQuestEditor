package com.example.demo.view

import com.example.demo.app.Styles
import javafx.beans.property.SimpleStringProperty
import javafx.collections.ObservableList
import tornadofx.*
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter

var slideTempId = SimpleStringProperty()
var removeSlideID = SimpleStringProperty()
var slideTempIdRemove = SimpleStringProperty()
var slideNumRemove = 0
var optionNumRemove = 0
var slideNum = 0
var optionNum = 0
var input = SimpleStringProperty()
const val numCol: Int = 5
var iTable = 2

class Slide(slideId: Int, slideText: String, val branch: ObservableList<Branch>){
    var slideId by property(slideId)
    fun slideIdProperty() = getProperty(Slide::slideId)

    var slideText by property(slideText)
    fun slideTextProperty() = getProperty(Slide::slideText)

}

class Branch(optionId: Int, optionText: String, optionAction: Int) {
    var optionId by property(optionId)
    fun optionIdProperty() = getProperty(Branch::optionId)
    var optionText by property(optionText)
    fun optionTextProperty() = getProperty(Branch::optionText)
    var optionAction by property(optionAction)
    fun optionActionProperty() = getProperty(Branch::optionAction)
}

var slides = mutableListOf(
        Slide(1,"This is starting slide", mutableListOf(
                Branch(1,"Example option №1",2),
                Branch(2,"Example option №2",2),
                Branch(3,"Example option №3",3)
        ).observable()),
        Slide(2,"This second slide", mutableListOf(
                Branch(1,"Example option №1",1),
                Branch(2,"Example option №2",1),
                Branch(3,"Example option №3",1)
        ).observable())
).observable()

class MainView : View("Quest Editor") {
    override val root = hbox {
        tableview(slides) {
            enableCellEditing()
            regainFocusAfterEdit()
            column("ID", Slide::slideIdProperty).fixedWidth(30).makeEditable()
            column("Slide Text", Slide::slideTextProperty).fixedWidth(270).makeEditable()
            rowExpander(expandOnDoubleClick = true) {
                paddingLeft = expanderColumn.width
                tableview(it.branch) {
                    column("Option ID", Branch::optionIdProperty).fixedWidth(70).makeEditable()
                    column("Option Text", Branch::optionTextProperty).fixedWidth(150).makeEditable()
                    column("Option Action", Branch::optionActionProperty).fixedWidth(70).makeEditable()
                }
            }
        }
        vbox {
            fieldset("Adding Slides/Options") {
                button("Create New Slide") {
                    useMaxWidth = true
                    action {
                        iTable += 1
                        val tempList = Slide(iTable, "", mutableListOf(
                                Branch(1, "", 2)
                        ).observable())
                        slides.add(tempList)
                    }
                }
                button("Create New Option") {
                    useMaxWidth = true
                    textfield(slideTempId)
                    action {
                        if (slideTempId.value.isNullOrBlank()) {
                            slideNum = slides.size
                            optionNum = slides[slideNum - 1].branch.size + 1
                            var tempOption = Branch(optionNum, "", 1)
                            slides[slideNum - 1].branch.add(tempOption)
                        } else {
                            var optionSlideNum = slideTempId.value.split(" ")
                            if (optionSlideNum.size != 2) {
                                slideNum = slides.size
                                optionNum = slides[slideNum - 1].branch.size + 1
                                var tempOption = Branch(optionNum, "", 1)
                                slides[slideNum - 1].branch.add(tempOption)
                            } else {
                                slideNum = optionSlideNum[0].toInt()
                                optionNum = optionSlideNum[1].toInt()
                                if (slideNum > slides.size) slideNum = slides.size
                                if (slideNum < 1) slideNum = 1
                                slides[slideNum - 1].branch.forEach { if (it.optionId == optionNum) optionNum = it.optionId + 1 }
                                var tempOption = Branch(optionNum, "", 1)
                                slides[slideNum - 1].branch.add(tempOption)
                            }
                        }
                    }
                }
            }
            fieldset("Remove Slides/Options") {
                button("Remove Slide") {
                    useMaxWidth = true
                    textfield(removeSlideID)
                    action {
                        iTable -= 1
                        if (!removeSlideID.value.isNullOrBlank()) {
                            var slideRemove = removeSlideID.value.toInt()
                            if (slideRemove > slides.size) slideRemove = slides.size
                            if (slideRemove < 1) slideRemove = 1
                            slides.remove(slides[slideRemove - 1])
                        } else {
                            var slideRemove = slides.size
                            slides.remove(slides[slideRemove - 1])
                        }
                    }
                }
                button("Remove Option") {
                    useMaxWidth = true
                    textfield(slideTempIdRemove)
                    action {
                        if (slideTempId.value.isNullOrBlank()) {
                            slideNumRemove = slides.size
                            optionNumRemove = slides[slideNumRemove - 1].branch.size
                            slides[slideNumRemove - 1].branch.remove(slides[slideNumRemove - 1].branch[optionNumRemove - 1])
                        } else {
                            var optionSlideNumRemove = slideTempIdRemove.value.split(" ")
                            if (optionSlideNumRemove.size != 2) {
                                slideNumRemove = slides.size
                                optionNumRemove = slides[slideNumRemove - 1].branch.size
                                slides[slideNumRemove - 1].branch.remove(slides[slideNumRemove - 1].branch[optionNumRemove - 1])
                            } else {
                                slideNumRemove = optionSlideNumRemove[0].toInt()
                                optionNumRemove = optionSlideNumRemove[1].toInt()
                                if (slideNumRemove > slides.size) slideNumRemove = slides.size
                                if (slideNumRemove < 1) slideNumRemove = 1
                                if (optionNumRemove > slides[slideNumRemove - 1].branch.size) optionNumRemove = slides[slideNumRemove - 1].branch.size
                                if (optionNumRemove < 1) optionNumRemove = 1
                                slides[slideNumRemove - 1].branch.remove(slides[slideNumRemove - 1].branch[optionNumRemove - 1])
                            }
                        }
                    }
                }
            }
            fieldset("File Operations") {
                button("New File") {
                    useMaxWidth = true
                    textfield(input)
                    action {
                        EditorController().createFile("res/${input.value}.txt")
                    }
                }
                button("Save File") {
                    useMaxWidth = true
                    action {
                        val outputFile = File("res/${input.value}.txt")
                        val writer = BufferedWriter(FileWriter(outputFile, true))
                        writer.write(" {\n" +
                                "    \"save\": {\n" +
                                "        \"slideId\" : 1\n" +
                                "    },\n" +
                                "\n" +
                                "    \"starting\" : {\n" +
                                "        \"slideId\" : 1\n" +
                                "    },\n" +
                                "\n" +
                                "    \"finishingId\" : ${slides.size}" +
                                "\n" +
                                "    \"slides\" : [")
                        for (i in 0 until slides.size) {
                            writer.write("\n        {" +
                                    "\n" +
                                    "            \"slideId\" : ${slides[i].slideId},\n" +
                                    "            \"text\" : ${slides[i].slideText},\n" +
                                    "            options : [")
                            for (k in 0 until slides[i].branch.size){
                                writer.write("{\n" +
                                        "                    \"optionId\" : ${slides[i].branch[k].optionId},\n" +
                                        "                    \"optionText\" : ${slides[i].branch[k].optionText},\n" +
                                        "                    \"moveToSlide\" : ${slides[i].branch[k].optionAction}\n" +
                                        "                }")
                            }
                            writer.write("\n            ]" +
                                    "\n" +
                                    "        }")
                        }
                        writer.write("\n    ]\n" +
                                "}")
                        writer.close()
                    }
                }
            }
        }
    }
}
class EditorController: Controller(){
    fun createFile(inputValue: String) = File(inputValue).createNewFile()
}
