package com.gmail.evgeniy.auth.view

import com.gmail.evgeniy.auth.AccessDeniedException
import com.gmail.evgeniy.auth.AuthService
import com.gmail.evgeniy.view.MainView
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.datepicker.DatePicker
import com.vaadin.flow.component.html.Label
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.router.BeforeEvent
import com.vaadin.flow.router.HasUrlParameter
import com.vaadin.flow.router.Route
import kotlinx.coroutines.runBlocking

@Route("start")
class StartView : VerticalLayout(), HasUrlParameter<String> {

    private val birthdayField = DatePicker("Введите дату рождения:")
    private val errorLabel = Label("Введеная дата ошибочна, попробуйте еще раз")
    private val button: Button = Button("Продолжить")
    private lateinit var token: String

    init {
        birthdayField.isRequired = true
        birthdayField.width = "100%"
        errorLabel.isVisible = false
        errorLabel.addClassName("birthday-error")

        birthdayField.addValueChangeListener { e -> button.isEnabled = e.value != null }
        button.addClickListener { checkBirthday() }
        button.width = "100%"
        add(birthdayField, errorLabel, button)
        maxWidth = "768px"
    }

    override fun setParameter(event: BeforeEvent, token: String) {
        runBlocking {
            if (AuthService.isTokenExists(token)) {
                event.ui.session.setAttribute("token", token)

                if (AuthService.isTokenAlive(token)) {
                    this@StartView.token = token
                } else {
                    AuthService.sendSmsForConfirmation(token)
                    event.rerouteTo(SmsAuthorizationView::class.java)
                }
            } else {
                throw AccessDeniedException("Невергая ссылка")
            }
        }
    }

    private fun checkBirthday() {
        if (runBlocking { AuthService.isCorrectBirthday(token, birthdayField.value) }) {
            errorLabel.isVisible = false
            ui.ifPresent {
                it.page.executeJs("return localStorage.setItem('token', $token)")
                it.navigate(MainView::class.java)
            }
        } else {
            errorLabel.isVisible = true
        }
    }

}