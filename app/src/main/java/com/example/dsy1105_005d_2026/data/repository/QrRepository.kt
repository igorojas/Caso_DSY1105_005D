package com.example.dsy1105_005d_2026.data.repository

import com.example.dsy1105_005d_2026.data.model.QrResult

class QrRepository {
    fun processQrContent(content: String): QrResult {
        // Aquí podrías guardar o procesar el QR en una BD o API
        return QrResult(content)
    }
}
