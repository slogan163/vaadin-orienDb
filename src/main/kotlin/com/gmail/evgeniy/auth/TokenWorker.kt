package com.gmail.evgeniy.auth

import com.gmail.evgeniy.auth.view.SmsAuthorizationView
import com.vaadin.flow.component.UI
import kotlinx.coroutines.runBlocking

object TokenWorker {

    fun authorize(ui: UI, actionAfterAuth: () -> Unit) {
        val token: String? = ui.session.getAttribute("token") as String?

        if (token != null) {
            processToken(token, ui, actionAfterAuth)
        } else {
            ui.page.executeJs("return localStorage.getItem('token')").then(String::class.java) { storageToken ->
                processToken(storageToken, ui, actionAfterAuth)
            }
        }
    }

    private fun processToken(token: String?, ui: UI, actionAfterAuth: () -> Unit) {
        if (token.isNullOrEmpty() || runBlocking { !AuthService.isTokenExists(token) }) {
            throw AccessDeniedException("Пожалуйста зарегистррируйтесь ...")
        }
        if (ui.session.getAttribute("token") == null) {
            ui.session.setAttribute("token", token)
        }

        if (runBlocking { !AuthService.isTokenAlive(token) }) {
            runBlocking { AuthService.sendSmsForConfirmation(token) }
            ui.navigate(SmsAuthorizationView::class.java)
        } else {
            actionAfterAuth.invoke()
        }
    }
}