package com.gmail.evgeniy.view

import com.gmail.evgeniy.auth.AuthService
import com.gmail.evgeniy.auth.authorize
import com.gmail.evgeniy.backend.BackendServiceLocal
import com.gmail.evgeniy.entity.Patient
import com.vaadin.flow.component.HasSize
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.datepicker.DatePicker
import com.vaadin.flow.component.dependency.CssImport
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.page.Push
import com.vaadin.flow.component.progressbar.ProgressBar
import com.vaadin.flow.component.textfield.TextArea
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.router.BeforeEnterEvent
import com.vaadin.flow.router.BeforeEnterObserver
import com.vaadin.flow.router.Route
import com.vaadin.flow.server.PWA
import com.vaadin.flow.theme.Theme
import com.vaadin.flow.theme.lumo.Lumo
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


@Route("patient")
@PWA(name = "Test", shortName = "Test", startPath = "patient")
@Theme(value = Lumo::class, variant = Lumo.LIGHT)
@CssImport("styles/main.css")
@Push
class MainView : VerticalLayout(), BeforeEnterObserver {

    private lateinit var patient: Patient

    private fun init() {
        val firstName = TextField("Имя")
        val lastName = TextField("Фамилия")
        val midName = TextField("Отчество")
        val birthday = DatePicker("День рождения")
        val email = TextField("Эл. почта")
        val notes = TextArea("Записи")
        val fieldBox = VerticalLayout()
        val buttonsBox = HorizontalLayout()
        val wrapper = VerticalLayout()
        val cancel = Button("Отменить")
        val save = Button("Сохранить")

        fieldBox.add(firstName, lastName, midName, birthday, email, notes)
        fieldBox.width = "100%"
        fieldBox.isSpacing = false
        fieldBox.isPadding = false
        fieldBox.children.forEach { (it as HasSize).width = "100%" }

        buttonsBox.add(save, cancel)
        buttonsBox.width = "100%"
        buttonsBox.children.forEach { (it as HasSize).width = "100%" }
        buttonsBox.addClassName("margin-top-20")
        buttonsBox.isSpacing = true

        wrapper.add(fieldBox, buttonsBox)
        wrapper.isSpacing = false
        wrapper.maxWidth = "768px"
        wrapper.width = "100%"
        add(wrapper)
        isPadding = false
        isSpacing = false

        firstName.value = patient.firstName
        lastName.value = patient.lastName
        midName.value = patient.midName ?: ""
        birthday.value = patient.birthday
        email.value = patient.email ?: ""
        notes.value = patient.notes ?: ""

        save.addClickListener { onSave(firstName, lastName, midName, birthday, email, notes) }
    }

    private fun onSave(firstName: TextField, lastName: TextField, midName: TextField, birthday: DatePicker,
                       email: TextField, notes: TextArea) {
        patient.firstName = firstName.value
        patient.lastName = lastName.value
        patient.midName = midName.value
        patient.birthday = birthday.value
        patient.email = email.value
        patient.notes = notes.value
        GlobalScope.launch { BackendServiceLocal.save(patient) }
    }

    override fun beforeEnter(event: BeforeEnterEvent) {
        val progressBar = showProgressBar()

        authorize(event.ui) { token: String ->
            val patientId: String = AuthService.getPatientId(token)
            val loadedPatient = BackendServiceLocal.load(patientId)

            event.ui.access {
                remove(progressBar)
                this.patient = loadedPatient
                init()
            }
        }
    }

    private fun showProgressBar(): ProgressBar {
        setSizeFull()
        justifyContentMode = FlexComponent.JustifyContentMode.CENTER
        defaultHorizontalComponentAlignment = FlexComponent.Alignment.CENTER

        val progressBar = ProgressBar()
        progressBar.isIndeterminate = true
        progressBar.width = "60%"
        add(progressBar)
        return progressBar
    }
}
