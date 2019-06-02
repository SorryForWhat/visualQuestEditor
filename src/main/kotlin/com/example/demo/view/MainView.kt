package com.example.demo.view

import com.example.demo.app.Styles
import javafx.beans.property.SimpleStringProperty
import javafx.collections.ObservableList
import tornadofx.*
import java.io.File

var input = SimpleStringProperty()
const val numCol: Int = 5
var iTable = 2
var iOption = 1
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
        Slide(1,"This is starting slide", listOf(
                Branch(1,"Example option №1",2),
                Branch(2,"Example option №2",2),
                Branch(3,"Example option №3",3)
        ).observable()),
        Slide(2,"This second slide", listOf(
                Branch(1,"Example option №1",1),
                Branch(2,"Example option №2",1),
                Branch(3,"Example option №3",1)
        ).observable())
).observable()
class MainView : View("Quest Editor") {
    override val root = hbox {

        tableview(slides) {
            column("Slide ID", Slide::slideIdProperty).makeEditable()
            column("Slide Text", Slide::slideTextProperty).makeEditable()
            rowExpander(expandOnDoubleClick = true) {
                paddingLeft = expanderColumn.width
                tableview(it.branch) {
                    column("Option ID", Branch::optionIdProperty).makeEditable()
                    column("Option Text", Branch::optionTextProperty).makeEditable()
                    column("Option Action", Branch::optionActionProperty).makeEditable()
                }
            }
        }

        button("Create New Slide") {
            action {
                iTable +=1
                val tempList = Slide(iTable,"", listOf(
                        Branch(1,"",2),
                        Branch(2,"",2),
                        Branch(3,"",3)
                ).observable())
                slides.add(tempList)
            }
        }

        button("New File") {
            useMaxWidth = true
            textfield(input)
            action {
                EditorController().createFile("res/${input.value}.txt")


            }
        }

    }
}
class EditorController: Controller(){
    fun createFile(inputValue: String) = File(inputValue).createNewFile()
}
//        button("Create New Option") {
//            action {
//                iOption +=1
//                val tempOption = Branch(iOption,"", 1)
//                it.branch.add(tempOption)
//            }
//        }