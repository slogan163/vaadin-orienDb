package com.gmail.evgeniy.view

import com.gmail.evgeniy.auth.AuthService
import com.gmail.evgeniy.auth.TokenWorker
import com.gmail.evgeniy.backend.BackendServiceLocal
import com.gmail.evgeniy.entity.Patient
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.datepicker.DatePicker
import com.vaadin.flow.component.dependency.CssImport
import com.vaadin.flow.component.html.Div
import com.vaadin.flow.component.html.H1
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.textfield.TextArea
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.router.BeforeEnterEvent
import com.vaadin.flow.router.BeforeEnterObserver
import com.vaadin.flow.router.Route
import com.vaadin.flow.server.PWA
import com.vaadin.flow.theme.Theme
import com.vaadin.flow.theme.lumo.Lumo


/**
 * The main view contains a button and a click listener.
 */
@Route("patient")
@PWA(name = "Test", shortName = "Test", startPath = "view/patient")
@Theme(value = Lumo::class, variant = Lumo.LIGHT)
@CssImport("styles/main.css")
class MainView : VerticalLayout(), BeforeEnterObserver {

    private val header = H1("Форма")
    private val firstName = TextField("Имя")
    private val lastName = TextField("Фамилия")
    private val midName = TextField("Отчество")
    private val birthday = DatePicker("День рождения")
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
        fieldBox.add(firstName, lastName, midName, birthday, email, notes)
        fieldBox.addClassName("fieldBox")

        buttonsBox.add(save, cancel)
        buttonsBox.addClassName("buttonBox")
        add(header, fieldBox, buttonsBox)
        addClassName("grid-container")

        save.addClickListener { onSave() }
    }

    override fun beforeEnter(event: BeforeEnterEvent) {
        TokenWorker.authorize(event.ui) {
            val token: String = event.ui.session.getAttribute("token") as String
            val patientId: String = AuthService.getPatientId(token)
            loadPatient(patientId)
        }
    }

    private fun onSave() {
        if (patient == null) {
            patient = Patient()
        }

        patient?.firstName = firstName.value
        patient?.lastName = lastName.value
        patient?.midName = midName.value
        patient?.birthday = birthday.value
        patient?.email = email.value
        patient?.notes = notes.value

        BackendServiceLocal.save(patient!!)
    }

    private fun loadPatient(patientId: String) {
        patient = BackendServiceLocal.load(patientId)
        firstName.value = patient?.firstName ?: ""
        lastName.value = patient?.lastName ?: ""
        midName.value = patient?.midName ?: ""
        birthday.value = patient?.birthday
        email.value = patient?.email ?: ""
        notes.value = patient?.notes ?: ""
    }
}
