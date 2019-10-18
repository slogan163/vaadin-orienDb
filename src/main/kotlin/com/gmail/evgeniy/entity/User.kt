package com.gmail.evgeniy.entity

import com.gmail.evgeniy.annotation.AllOpen
import java.time.LocalDateTime

@AllOpen
data class User(@Volatile var patientId: String,
                @Volatile var token: String,
                @Volatile var tokenExpirationTime: LocalDateTime,
                @Volatile var isActive: Boolean)