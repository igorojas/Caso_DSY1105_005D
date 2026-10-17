package com.example.dsy1105_005d_2026.data.repository

import com.example.dsy1105_005d_2026.data.local.SolicitudDao
import com.example.dsy1105_005d_2026.data.local.SolicitudEntity
import kotlinx.coroutines.flow.Flow

class SolicitudRepository(private val dao: SolicitudDao) {
    val solicitudes: Flow<List<SolicitudEntity>> = dao.obtenerTodas()

    suspend fun obtener(id: Int): SolicitudEntity? = dao.obtenerPorId(id)

    suspend fun guardar(solicitud: SolicitudEntity) = dao.guardar(solicitud)
}
