package com.gmail.evgeniy.view

import com.gmail.evgeniy.auth.AuthService
import com.gmail.evgeniy.auth.authorize
import com.gmail.evgeniy.backend.BackendServiceLocal
import com.gmail.evgeniy.entity.Patient
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.datepicker.DatePicker
import com.vaadin.flow.component.dependency.CssImport
import com.vaadin.flow.component.html.Div
import com.vaadin.flow.component.html.H1
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.page.Push
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


/**
 * The main view contains a button and a click listener.
 */
@Route("patient")
@PWA(name = "Test", shortName = "Test", startPath = "patient")
@Theme(value = Lumo::class, variant = Lumo.LIGHT)
@CssImport("styles/main.css")
@Push
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
    private val wrapper = VerticalLayout()
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
        wrapper.add(header, fieldBox, buttonsBox)
        wrapper.addClassName("grid-container")
        add(wrapper)
        justifyContentMode = FlexComponent.JustifyContentMode.CENTER
        defaultHorizontalComponentAlignment = FlexComponent.Alignment.CENTER

        save.addClickListener { onSave() }
    }

    override fun beforeEnter(event: BeforeEnterEvent) {
        authorize(event.ui) { token: String ->
            val patientId: String = AuthService.getPatientId(token)
            val loadedPatient = BackendServiceLocal.load(patientId)

            event.ui.access {
                patient = loadedPatient
                firstName.value = patient?.firstName ?: ""
                lastName.value = patient?.lastName ?: ""
                midName.value = patient?.midName ?: ""
                birthday.value = patient?.birthday
                email.value = patient?.email ?: ""
                notes.value = patient?.notes ?: ""
            }
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

        GlobalScope.launch { BackendServiceLocal.save(patient!!) }
    }
}
