package com.gmail.evgeniy.backend

import java.util.*

data class Patient(val id: UUID = UUID.randomUUID(),
                   var firstName: String,
                   var lastName: String,
                   var midName: String? = null,
                   var title: String? = null,
                   var email: String? = null,
                   var notes: String? = null) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other?.javaClass != javaClass) return false

        return this.id == (other as Patient).id
    }

    override fun hashCode(): Int {
        return this.id.hashCode()
    }
}
