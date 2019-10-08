package com.gmail.evgeniy.backend

import com.gmail.evgeniy.annotation.AllOpen
import java.io.Serializable

@AllOpen
open class Hospital(var name: String = "", val patients: MutableList<Patient> = mutableListOf()) : Serializable {
}