package com.gmail.evgeniy.auth.view

import com.gmail.evgeniy.auth.AuthService
import com.gmail.evgeniy.view.MainView
import com.vaadin.flow.component.HasValue
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.notification.Notification
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.textfield.PasswordField
import com.vaadin.flow.data.value.ValueChangeMode
import com.vaadin.flow.router.BeforeEnterEvent
import com.vaadin.flow.router.BeforeEnterObserver
import com.vaadin.flow.router.Route
import kotlinx.coroutines.runBlocking

@Route("smsAuthorization")
class SmsAuthorizationView : VerticalLayout(), BeforeEnterObserver {

    private val passwordField = PasswordField("Код подтверждения")
    private val resendLink = Button("Отправить еще раз")

    private lateinit var token: String

    init {
        passwordField.isClearButtonVisible = true
        passwordField.pattern = "^\\d{0,4}$"
        passwordField.isPreventInvalidInput = true
        passwordField.errorMessage = "Код не верный, попробуйте еще раз"
        passwordField.valueChangeMode = ValueChangeMode.TIMEOUT
        passwordField.addValueChangeListener { onPasswordChanged(it) }

        //todo: add style for resendLink
        resendLink.addClickListener { onResendClick() }
        add(passwordField, resendLink)
    }

    private fun onResendClick() {
        runBlocking { AuthService.sendSmsForConfirmation(token) }
        Notification("смс отправлено", 3000).open()
    }

    private fun onPasswordChanged(event: HasValue.ValueChangeEvent<String>) {
        passwordField.isInvalid = false

        if (event.value.length == 4) {
            if (runBlocking { AuthService.checkPassword(token, passwordField.value) }) {
                runBlocking { AuthService.updateToken(token) }
                ui.ifPresent { it.navigate(MainView::class.java) }
            } else {
                passwordField.isInvalid = true
            }
        }
    }

    override fun beforeEnter(event: BeforeEnterEvent) {
        token = event.ui.session.getAttribute("token") as String
    }
}