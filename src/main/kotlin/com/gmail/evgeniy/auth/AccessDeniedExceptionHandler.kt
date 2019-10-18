package com.gmail.evgeniy.auth

import com.vaadin.flow.component.Component
import com.vaadin.flow.component.Tag
import com.vaadin.flow.router.BeforeEnterEvent
import com.vaadin.flow.router.ErrorParameter
import com.vaadin.flow.router.HasErrorParameter
import javax.servlet.http.HttpServletResponse

@Tag(Tag.DIV)
class AccessDeniedExceptionHandler : Component(), HasErrorParameter<AccessDeniedException> {

    override fun setErrorParameter(event: BeforeEnterEvent, parameter: ErrorParameter<AccessDeniedException>): Int {
        element.text = parameter.customMessage
        return HttpServletResponse.SC_FORBIDDEN
    }

}