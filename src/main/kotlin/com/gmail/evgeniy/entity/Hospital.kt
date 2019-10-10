package com.gmail.evgeniy.entity

import com.gmail.evgeniy.annotation.AllOpen

@AllOpen
class Hospital {
    var name: String = ""
    val patients: MutableList<Patient> = mutableListOf()
}