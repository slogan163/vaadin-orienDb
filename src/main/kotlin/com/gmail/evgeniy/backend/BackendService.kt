package com.gmail.evgeniy.backend

import java.util.*

interface BackendService {

    fun getEmployees(): Set<Patient>

    fun save(patient: Patient)

    fun load(id: UUID): Patient
}