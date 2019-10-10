package com.gmail.evgeniy.entity

import com.gmail.evgeniy.annotation.AllOpen
import com.orientechnologies.orient.core.id.ORID
import javax.persistence.Id
import javax.persistence.OneToMany

@AllOpen
class Hospital {

    @Id
    var rid: ORID? = null
    var name: String = ""
    @OneToMany
    val patients: MutableList<Patient> = mutableListOf()
}