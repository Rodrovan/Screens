// En tu proyecto Android, por ejemplo: app/src/main/java/com/example/yourapp/model/Facultad.kt
package com.example.screens.model // Ajusta el package a tu estructura

enum class Facultad(val displayName: String) {
    CIENCIAS_ADMINISTRATIVAS_Y_ECONOMICAS("Ciencias Administrativas y Económicas"),
    CIENCIAS_JURIDICAS_HUMANIDADES_Y_RELACIONES_INTERNACIONALES("Ciencias Jurídicas Humanidades y Relaciones Internacionales"),
    CIENCIAS_MEDICAS("Ciencias Médicas"),
    INGENIERIA_Y_ARQUITECTURA("Ingeniería y Arquitectura"),
    MARKETING_DISENO_Y_CIENCIAS_DE_LA_COMUNICACION("Marketing Diseño y Ciencias de la Comunicación"),
    UAM_COLLEGE("UAM COLLEGE"),
    ODONTOLOGIA("Odontología");

    // Opcional: si necesitas obtener el enum a partir del displayName
    companion object {
        fun fromDisplayName(name: String): Facultad? {
            return values().find { it.displayName.equals(name, ignoreCase = true) }
        }
    }
}
    