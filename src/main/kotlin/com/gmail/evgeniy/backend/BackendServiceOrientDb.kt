package com.gmail.evgeniy.backend

import com.gmail.evgeniy.entity.Patient
import com.orientechnologies.orient.`object`.db.OObjectDatabaseTx
import com.orientechnologies.orient.core.db.OPartitionedDatabasePool
import com.orientechnologies.orient.core.id.ORecordId
import com.orientechnologies.orient.core.sql.query.OSQLSynchQuery
import java.util.stream.Collectors.toSet


object BackendServiceOrientDb : BackendService {

    private val pool: OPartitionedDatabasePool

    init {
        val databases ="C:\\Users\\sloga\\Downloads\\my-starter-project\\databases"
        pool = OPartitionedDatabasePool("plocal:${databases}", "admin", "admin")

        OObjectDatabaseTx(pool.acquire()).use { db ->
            db.isAutomaticSchemaGeneration = true
            db.entityManager.registerEntityClasses("com.gmail.evgeniy.entity")
        }
    }

    override fun getEmployees(): Set<Patient> {
        OObjectDatabaseTx(pool.acquire().activateOnCurrentThread()).use {
            return it.query<List<Patient>>(OSQLSynchQuery<Patient>("select from Patient")).stream().collect(toSet())
        }
    }

    override fun save(patient: Patient) {
        OObjectDatabaseTx(pool.acquire().activateOnCurrentThread()).use {
            it.attachAndSave<Patient>(patient)
        }
    }

    override fun load(id: String): Patient? {
        OObjectDatabaseTx(pool.acquire().activateOnCurrentThread()).use {
            val proxy = it.load<Patient>(ORecordId(id))

            val patient = Patient()
            patient.rid = proxy.rid
            patient.firstName = proxy.firstName
            patient.lastName = proxy.lastName
            patient.midName = proxy.midName
            patient.email = proxy.email
            patient.notes = proxy.notes
            return patient
        }
    }
}