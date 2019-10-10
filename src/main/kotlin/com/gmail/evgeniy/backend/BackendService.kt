package com.gmail.evgeniy.backend

import com.gmail.evgeniy.entity.Hospital
import com.gmail.evgeniy.entity.Patient

interface BackendService {

    fun getEmployees(): Set<Patient>

    fun save(patient: Patient)

    fun load(id: String): Patient?
    fun loadHospital(name: String): Hospital?
    fun saveHospital(hospital: Hospital)
}