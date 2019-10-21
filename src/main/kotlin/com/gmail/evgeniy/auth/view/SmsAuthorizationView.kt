package com.gmail.evgeniy.auth.view

import com.gmail.evgeniy.auth.AuthService
import com.gmail.evgeniy.auth.TooOftenSendingException
import com.gmail.evgeniy.view.MainView
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
    private val confirmBtn = Button("Подтвердить")

    private lateinit var token: String

    init {
        passwordField.isClearButtonVisible = true
        passwordField.pattern = "^\\d{0,4}$"
        passwordField.isPreventInvalidInput = true
        passwordField.errorMessage = "Код не верный, попробуйте еще раз"
        passwordField.valueChangeMode = ValueChangeMode.TIMEOUT
        passwordField.width = "100%"
//        passwordField.addValueChangeListener { onPasswordChanged(it.value) }
        confirmBtn.addClickListener { onPasswordChanged(passwordField.value) }
        confirmBtn.width = "100%"

        //todo: add style for resendLink
        resendLink.addClickListener { onResendClick() }
        resendLink.width = "100%"
        //todo: block the button for 60 seconds after resending a sms

        add(passwordField, confirmBtn, resendLink)
        maxWidth = "768px"
    }

    private fun onResendClick() {
        runBlocking {
            try {
                AuthService.sendSmsForConfirmation(token)
                Notification("смс отправлено", 3000).open()
            } catch (e: TooOftenSendingException) {
                Notification("смс отправляется слишком часто, попробуйте через 60 секунд", 3000).open()
            }
        }
    }

    private fun onPasswordChanged(password: String) {
        passwordField.isInvalid = false

        if (password.length == 4) {
            runBlocking {
                if (AuthService.checkPassword(token, passwordField.value)) {
                    AuthService.updateToken(token)
                    ui.ifPresent { it.navigate(MainView::class.java) }
                } else {
                    passwordField.isInvalid = true
                }
            }
        }
    }

    override fun beforeEnter(event: BeforeEnterEvent) {
        token = event.ui.session.getAttribute("token") as String
    }
}