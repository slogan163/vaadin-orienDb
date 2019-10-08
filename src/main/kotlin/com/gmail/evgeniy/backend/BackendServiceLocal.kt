package com.gmail.evgeniy.backend

import java.util.*


object BackendServiceLocal : BackendService {

    private val patients = mutableSetOf<Patient>()

    init {
//        patients.add(Patient(UUID.fromString("e87dc893-f6cf-478e-8d7b-30639ed70436").toString(), "Rowena", "Leeming", null, null,"rleeming0@bbc.co.uk", "Food Chemist"))
//        patients.add(Patient(UUID.fromString("7489fdcd-a056-4447-9a3a-c6199c89288c").toString(), "Alvinia", "Delong", null,null, "adelong1@altervista.org", "Recruiting Manager"))
//        patients.add(Patient(UUID.fromString("bb18583e-af29-4f9c-94c6-037a08a47ee9").toString(), "Leodora", "Burry", null,null, "lburry2@example.com", "Food Chemist"))
//        patients.add(Patient(UUID.fromString("1fd2671d-c02f-4576-8200-0414c9d30ad9").toString(), "Karen", "Oaten", null, null,"koaten3@ihg.com", "VP Sales"))
    }

    override fun getEmployees(): Set<Patient> {
        return patients
    }

    override fun save(patient: Patient) {
        patients.add(patient)
    }

    override fun load(id: UUID): Patient {
        return patients.firstOrNull { id.toString() == it.specialId } ?: throw RuntimeException("No patient with id: $id")
    }
}
