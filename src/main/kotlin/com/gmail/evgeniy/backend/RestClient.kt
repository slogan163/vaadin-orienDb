package com.gmail.evgeniy.backend

import com.gmail.evgeniy.entity.Patient
import io.ktor.client.HttpClient
import io.ktor.client.engine.apache.Apache
import io.ktor.client.features.json.JacksonSerializer
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.request.get
import io.ktor.client.request.post
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

object RestClient {
    private const val HOST: String = "http://127.0.0.1:9900"
    val json = io.ktor.client.features.json.defaultSerializer()

    private fun newClient(): HttpClient {
        return HttpClient(Apache) {
            install(JsonFeature) {
                serializer = JacksonSerializer()
                //gson throws error by serialiation
//                serializer = GsonSerializer()
            }
        }
    }

    suspend fun getEmployees(): Set<Patient> {
        return GlobalScope.async {
            newClient().use {
                it.get<List<Patient>>("${HOST}/patients").toSet()
            }
        }.await()
    }

    suspend fun save(patient: Patient) {
        newClient().use {
            return GlobalScope.async {
                it.post<Unit>("$HOST/patient") {
                    body = json.write(patient)
                }
            }.await()
        }
    }

    suspend fun load(id: String): Patient? {
        val split = id.split(":")
        newClient().use {
            return GlobalScope.async { it.get<Patient>("$HOST/patient?clusterId=${split[0]}&id=${split[1]}") }.await()
        }
    }
}