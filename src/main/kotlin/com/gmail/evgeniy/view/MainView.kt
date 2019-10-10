package com.gmail.evgeniy.view

import com.gmail.evgeniy.backend.BackendServiceOrientDb
import com.gmail.evgeniy.entity.Patient
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.dependency.CssImport
import com.vaadin.flow.component.html.Div
import com.vaadin.flow.component.html.H1
import com.vaadin.flow.component.notification.Notification
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.page.Page
import com.vaadin.flow.component.textfield.TextArea
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.router.BeforeEvent
import com.vaadin.flow.router.HasUrlParameter
import com.vaadin.flow.router.OptionalParameter
import com.vaadin.flow.router.Route
import com.vaadin.flow.server.PWA
import com.vaadin.flow.theme.Theme
import com.vaadin.flow.theme.lumo.Lumo


/**
 * The main view contains a button and a click listener.
 */
@Route("patient")
@PWA(name = "Test", shortName = "Test", startPath = "patient/")
@Theme(value = Lumo::class, variant = Lumo.LIGHT)
@CssImport("styles/main.css")
class MainView : VerticalLayout(), HasUrlParameter<String> {

    private val header = H1("Форма")
    private val firstName = TextField("Имя")
    private val lastName = TextField("Фамилия")
    private val midName = TextField("Отчество")
    private val email = TextField("Эл. почта")
    private val notes = TextArea("Записи")
    private val fieldBox = Div()
    private val buttonsBox = Div()
    private var patient: Patient? = null

    private val cancel = Button("Отменить")
    private val save = Button("Сохранить")

    init {
        this.isSpacing = false

        header.classNames.add("header")
        fieldBox.add(firstName, lastName, midName, email, notes)
        fieldBox.addClassName("fieldBox")

        buttonsBox.add(save, cancel)
        buttonsBox.addClassName("buttonBox")
        add(header, fieldBox, buttonsBox)
        addClassName("grid-container")

        save.addClickListener {
            if(patient == null){
                patient = Patient()
            }

            patient?.firstName = firstName.value
            patient?.lastName = lastName.value
            patient?.midName = midName.value
            patient?.email = email.value
            patient?.notes = notes.value

            BackendServiceOrientDb.save(patient!!)
        }
    }

    override fun setParameter(event: BeforeEvent, @OptionalParameter parameter: String?) {
        if (parameter != null) {
            loadPatient("#$parameter")
            storePatientId(UI.getCurrent().page, "#$parameter")
        } else {
            loadPatientId(UI.getCurrent().page)
            Notification.show("No parameter")
        }
    }

    private fun storePatientId(page: Page, patientId: String) {
        page.executeJs(String.format("localStorage.setItem('patientId', '%s')", patientId))
    }

    private fun loadPatientId(page: Page) {
        page.executeJs("return localStorage.getItem('patientId')")
                .then(String::class.java) { this.loadPatient(it) }
    }

    private fun loadPatient(patientId: String) {
        patient = BackendServiceOrientDb.load(patientId)

        firstName.value = patient?.firstName ?: ""
        lastName.value = patient?.lastName ?: ""
        midName.value = patient?.midName ?: ""
        email.value = patient?.email ?: ""
        notes.value = patient?.notes ?: ""
    }
}
