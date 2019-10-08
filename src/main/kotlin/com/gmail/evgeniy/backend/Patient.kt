package com.gmail.evgeniy.backend

import com.gmail.evgeniy.annotation.AllOpen

@AllOpen
class Patient() {

    var specialId: String = ""
    var firstName: String = ""
    var lastName: String = ""
    var hospital: Hospital? = null
    var midName: String? = null
    var title: String? = null
    var email: String? = null
    var notes: String? = null

    init {

    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other?.javaClass != javaClass) return false

        return this.specialId == (other as Patient).specialId
    }

    override fun hashCode(): Int {
        return this.specialId.hashCode()
    }
}
