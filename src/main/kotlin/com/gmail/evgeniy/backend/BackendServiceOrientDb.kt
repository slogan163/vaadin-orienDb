package com.gmail.evgeniy.backend

import com.orientechnologies.orient.`object`.db.OObjectDatabaseTx
import com.orientechnologies.orient.core.db.OPartitionedDatabasePool
import com.orientechnologies.orient.core.sql.query.OSQLSynchQuery
import java.io.File
import java.util.*
import java.util.stream.Collectors.toSet


object BackendServiceOrientDb : BackendService {

    val pool: OPartitionedDatabasePool

    init {
        val databases = File("databases")
        pool = OPartitionedDatabasePool("plocal:${databases.absolutePath}", "admin", "admin")


        val db = OObjectDatabaseTx(pool.acquire())
        try {
            db.isAutomaticSchemaGeneration = true
            db.entityManager.registerEntityClass(Patient::class.java)
            db.begin()

            var list = db.query<List<Patient>>(OSQLSynchQuery<Patient>("select from Patient"))
            if (list.isNotEmpty()) {
                list.forEach { db.delete(it) }
            }
            list = db.query<List<Patient>>(OSQLSynchQuery<Patient>("select from Patient "))

            val patient = db.newInstance(Patient::class.java)
            patient.specialId = "e87dc893-f6cf-478e-8d7b-30639ed70436"
            patient.firstName = "Ivan"
            patient.lastName = "Ivanov"

            db.save<Patient>(patient)
            db.commit()

            val collect = db.query<List<Patient>>(OSQLSynchQuery<Patient>("select from Patient")).stream().collect(toSet())
            println()

        } finally {
            db.close()
        }
    }

//    private fun loadEntities() {
//        db.save<Patient>)
//        db.save<Patient>(Patient(UUID.fromString("7489fdcd-a056-4447-9a3a-c6199c89288c").toString(), "Alvinia", "Delong", null, null, "adelong1@altervista.org", "Recruiting Manager"))
//        db.save<Patient>(Patient(UUID.fromString("bb18583e-af29-4f9c-94c6-037a08a47ee9").toString(), "Leodora", "Burry", null, null, "lburry2@example.com", "Food Chemist"))
//        db.save<Patient>(Patient(UUID.fromString("1fd2671d-c02f-4576-8200-0414c9d30ad9").toString(), "Karen", "Oaten", null, null, "koaten3@ihg.com", "VP Sales"))
//    }

    override fun getEmployees(): Set<Patient> {
        val db = OObjectDatabaseTx(pool.acquire())
        try {
            return db.query<List<Patient>>(OSQLSynchQuery<Patient>("select from Patient")).stream().collect(toSet())
        } finally {
            db.close()
        }
    }

    override fun save(patient: Patient) {
        val db = OObjectDatabaseTx(pool.acquire())
        try {
            db.attachAndSave<Patient>(patient)
            db.commit()
        } finally {
            db.close()
        }
    }

    override fun load(id: UUID): Patient {
        val db = OObjectDatabaseTx(pool.acquire())
        try {
            val list = db.query<List<Patient>>(OSQLSynchQuery<Patient>("select * from Patient where specialId = ?"), id.toString())
            if (list.size == 1) {
                return list[0]
            }

            throw RuntimeException("Can not find patient with id: $id")
        } finally {
            db.close()
        }
    }
}