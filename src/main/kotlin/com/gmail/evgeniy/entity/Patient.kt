package com.gmail.evgeniy.entity

import com.gmail.evgeniy.annotation.AllOpen
import com.orientechnologies.orient.core.id.ORID
import javax.persistence.Id
import javax.persistence.ManyToOne

@AllOpen
class Patient {

    @Id var rid: ORID? = null
    var firstName: String = ""
    var lastName: String = ""
    var midName: String? = null
    var title: String? = null
    var email: String? = null
    var notes: String? = null
    @ManyToOne
    var hospital: Hospital? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other?.javaClass != javaClass) return false

        return this.rid == (other as Patient).rid
    }

    override fun hashCode(): Int {
        return this.rid.hashCode()
    }
}
