package com.gmail.evgeniy.auth.view

import com.gmail.evgeniy.auth.AuthService
import com.gmail.evgeniy.auth.CustomException
import com.gmail.evgeniy.auth.doAfterAccess
import com.gmail.evgeniy.view.MainView
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.notification.Notification
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.page.Push
import com.vaadin.flow.component.textfield.PasswordField
import com.vaadin.flow.data.value.ValueChangeMode
import com.vaadin.flow.router.BeforeEnterEvent
import com.vaadin.flow.router.BeforeEnterObserver
import com.vaadin.flow.router.Route
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@Push
@Route("smsAuthorization")
class SmsAuthorizationView : VerticalLayout(), BeforeEnterObserver {

    private val passwordField = PasswordField("Код подтверждения")
    private val resendLink = Button("Отправить еще раз")
    private val confirmBtn = Button("Подтвердить")
    private val wrapper = VerticalLayout()

    private lateinit var token: String

    private fun init() {
        passwordField.isClearButtonVisible = true
        passwordField.pattern = "^\\d{0,4}$"
        passwordField.isPreventInvalidInput = true
        passwordField.errorMessage = "Код не верный, попробуйте еще раз"
        passwordField.valueChangeMode = ValueChangeMode.TIMEOUT
        passwordField.width = "100%"
        confirmBtn.addClickListener { onPasswordChanged(passwordField.value) }
        confirmBtn.width = "100%"

        resendLink.addClickListener { onResendClick() }
        resendLink.width = "100%"

        wrapper.add(passwordField, confirmBtn, resendLink)
        wrapper.maxWidth = "768px"
        add(wrapper)
        justifyContentMode = FlexComponent.JustifyContentMode.CENTER
        defaultHorizontalComponentAlignment = FlexComponent.Alignment.CENTER
    }

    private fun onResendClick() {
        GlobalScope.launch {
            try {
                AuthService.sendSmsForConfirmation(token)
                ui.doAfterAccess { Notification("смс отправлено", 3000).open() }
            } catch (e: CustomException) {
                ui.doAfterAccess { Notification(e.localizedMessage, 3000).open() }
            }
        }
    }

    private fun onPasswordChanged(password: String) {
        passwordField.isInvalid = false

        if (password.length == 4) {
            GlobalScope.launch {
                if (AuthService.checkPassword(token, passwordField.value)) {
                    AuthService.updateToken(token)
                    ui.doAfterAccess { ui.get().navigate(MainView::class.java) }
                } else {
                    ui.doAfterAccess { passwordField.isInvalid = true }
                }
            }
        }
    }

    override fun beforeEnter(event: BeforeEnterEvent) {
        token = event.ui.session.getAttribute("token") as String
        init()
    }
}