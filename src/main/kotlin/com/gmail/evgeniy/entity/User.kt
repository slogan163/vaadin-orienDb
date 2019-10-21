package com.gmail.evgeniy.entity

import com.gmail.evgeniy.annotation.AllOpen
import java.time.LocalDateTime

@AllOpen
data class User(val patientId: String,
                val token: String,
                val tokenExpirationTime: LocalDateTime,
                val isActive: Boolean)