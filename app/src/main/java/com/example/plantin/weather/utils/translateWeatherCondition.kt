package com.example.plantin.weather.utils

/*
 * Fungsi untuk menerjemahkan kondisi cuaca ke bahasa Indonesia
 */
fun translateWeatherCondition(englishCondition: String): String {
    val translationMap = mapOf(
        "Clear" to "Cerah",
        "Clouds" to "Berawan",
        "Rain" to "Hujan",
        "Thunderstorm" to "Badai",
        "Drizzle" to "Gerimis",
        "Snow" to "Salju",
        "Mist" to "Kabut",
        "Haze" to "Kabut Tipis",
        "Fog" to "Kabut Tebal",
        "light rain" to "Hujan Ringan",
        "broken clouds" to "Berkabut",
        "few clouds" to "Seberang Berawan",
        "moderate rain" to "Hujan Sedang",
        "heavy intensity rain" to "Hujan Berat",
        "overcast clouds" to "Berawan Tebal",
    )

    return translationMap[englishCondition] ?: englishCondition
}