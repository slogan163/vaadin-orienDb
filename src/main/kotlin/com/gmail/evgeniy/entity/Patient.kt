package com.gmail.evgeniy.entity

import com.gmail.evgeniy.annotation.AllOpen
import java.time.LocalDate

@AllOpen
data class Patient(var rid: String = "#00:00",
                   var firstName: String = "",
                   var lastName: String = "",
                   var midName: String? = null,
                   var phoneNumber: String? = null,
                   var birthday: LocalDate? = null,
                   var title: String? = null,
                   var email: String? = null,
                   var notes: String? = null)
