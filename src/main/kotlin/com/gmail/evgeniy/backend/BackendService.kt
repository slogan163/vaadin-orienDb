package com.gmail.evgeniy.backend

import com.gmail.evgeniy.entity.Patient

interface BackendService {

    fun getEmployees(): Set<Patient>

    fun save(patient: Patient)

    fun load(id: String): Patient?
}