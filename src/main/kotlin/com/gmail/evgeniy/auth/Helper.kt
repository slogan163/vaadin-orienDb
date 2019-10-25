package com.gmail.evgeniy.auth

import com.gmail.evgeniy.auth.view.SmsAuthorizationView
import com.vaadin.flow.component.UI
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*

fun authorize(ui: UI, actionAfterAuth: suspend (token: String) -> Unit) {
    val token: String? = ui.session.getAttribute("token") as String?

    if (token != null) {
        processToken(token, ui, actionAfterAuth)
    } else {
        ui.page.executeJs("return localStorage.getItem('token')").then(String::class.java) { storageToken ->
            processToken(storageToken, ui, actionAfterAuth)
        }
    }
}

internal fun Optional<UI>.doAfterAccess(action: () -> Unit) {
    orElse(null)?.access { action.invoke() }
}

private fun processToken(token: String?, ui: UI, actionAfterAuth: suspend (token: String) -> Unit) {
    GlobalScope.launch {
        if (token.isNullOrEmpty() || !AuthService.isTokenExists(token)) {
            throw AccessDeniedException("Пожалуйста зарегистррируйтесь ...")
        }
        if (ui.session.getAttribute("token") == null) {
            ui.session.setAttribute("token", token)
        }

        if (!AuthService.isTokenAlive(token)) {
            AuthService.sendSmsForConfirmation(token)
            ui.access { ui.navigate(SmsAuthorizationView::class.java) }
        } else {
            actionAfterAuth.invoke(token)
        }
    }
}
