package com.gmail.evgeniy.auth.view

import com.gmail.evgeniy.auth.AccessDeniedException
import com.gmail.evgeniy.auth.AuthService
import com.gmail.evgeniy.auth.doAfterAccess
import com.gmail.evgeniy.view.MainView
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.datepicker.DatePicker
import com.vaadin.flow.component.html.Label
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.page.Push
import com.vaadin.flow.router.BeforeEvent
import com.vaadin.flow.router.HasUrlParameter
import com.vaadin.flow.router.Route
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@Route("start")
@Push
class StartView : VerticalLayout(), HasUrlParameter<String> {

    private val birthdayField = DatePicker("Введите дату рождения:")
    private val errorLabel = Label("Введеная дата ошибочна, попробуйте еще раз")
    private val button: Button = Button("Продолжить")
    private val wrapper: VerticalLayout = VerticalLayout()

    private var token: String? = null

    init {
        birthdayField.isRequired = true
        birthdayField.width = "100%"
        errorLabel.isVisible = false
        errorLabel.addClassName("birthday-error")

        birthdayField.addValueChangeListener { e -> button.isEnabled = e.value != null }
        button.addClickListener { checkBirthday() }
        button.width = "100%"
        wrapper.add(birthdayField, errorLabel, button)
        wrapper.maxWidth = "768px"

        add(wrapper)
        setSizeFull()
        justifyContentMode = FlexComponent.JustifyContentMode.CENTER
        defaultHorizontalComponentAlignment = FlexComponent.Alignment.CENTER
    }

    override fun setParameter(event: BeforeEvent, paramToken: String) {
        GlobalScope.launch {
            if (AuthService.isTokenExists(paramToken)) {
                event.ui.session.setAttribute("token", paramToken)

                if (AuthService.isTokenAlive(paramToken)) {
                    event.ui.access { token = paramToken }
                } else {
                    AuthService.sendSmsForConfirmation(paramToken)
                    event.ui.navigate(SmsAuthorizationView::class.java)
                }
            } else {
                throw AccessDeniedException("Невергая ссылка")
            }
        }

    }

    private fun checkBirthday() {
        GlobalScope.launch {
            if (token != null && AuthService.isCorrectBirthday(token!!, birthdayField.value)) {
                ui.orElse(null)?.page?.executeJs("localStorage.setItem('token', $token)")
                ui.orElse(null)?.navigate(MainView::class.java)
            } else {
                ui.doAfterAccess { errorLabel.isVisible = true }
            }
        }
    }
}