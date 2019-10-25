package com.gmail.evgeniy.backend

import com.gmail.evgeniy.auth.AccessDeniedException
import com.gmail.evgeniy.entity.Hospital
import com.gmail.evgeniy.entity.Patient
import java.time.LocalDate
import java.util.*


object BackendServiceLocal : BackendService {
    override fun loadHospital(name: String): Hospital? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun saveHospital(hospital: Hospital) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private val patients = mutableSetOf<Patient>()

    init {
        patients.add(Patient(UUID.fromString("e87dc893-f6cf-478e-8d7b-30639ed70436").toString(), "Rowena", "Leeming", null, "666", LocalDate.parse("2007-12-03"), "rleeming0@bbc.co.uk", "Food Chemist"))
        patients.add(Patient(UUID.fromString("7489fdcd-a056-4447-9a3a-c6199c89288c").toString(), "Alvinia", "Delong", null, "999", LocalDate.parse("2007-12-03"), "adelong1@altervista.org", "Recruiting Manager"))
        patients.add(Patient(UUID.fromString("bb18583e-af29-4f9c-94c6-037a08a47ee9").toString(), "Leodora", "Burry", null, "111", LocalDate.parse("2007-12-03"), "lburry2@example.com", "Food Chemist"))
        patients.add(Patient(UUID.fromString("1fd2671d-c02f-4576-8200-0414c9d30ad9").toString(), "Karen", "Oaten", null, "777", LocalDate.parse("2007-12-03"), "koaten3@ihg.com", "VP Sales"))
    }

    override fun getEmployees(): Set<Patient> {
        return patients
    }

    override fun save(patient: Patient) {
        patients.add(patient)
    }

    override fun load(id: String): Patient {
        return patients.firstOrNull { id == it.rid }
                ?: throw AccessDeniedException("Пользователя не существует в системе")
    }
}
