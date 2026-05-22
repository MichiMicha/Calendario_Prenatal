package com.example.myapplication.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.UserPreferences
import com.example.myapplication.data.dataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class UserViewModel(application: Application) : AndroidViewModel(application) {

    private val userPreferences = UserPreferences(application)


    private val _userName = MutableStateFlow("")
    val userName: StateFlow<String> = _userName

    private val _weight = MutableStateFlow("")
    val weight: StateFlow<String> = _weight

    private val _pregnancyDateMillis = MutableStateFlow<Long?>(null)
    val pregnancyDateMillis: StateFlow<Long?> = _pregnancyDateMillis

    val formattedPregnancyDate: String
        get() = _pregnancyDateMillis.value?.let { millis ->
            val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            formatter.format(Date(millis))
        } ?: ""


    val currentWeek = MutableStateFlow(1)
    val daysRemaining = MutableStateFlow(266)
    val dueDate = MutableStateFlow("")
    val todayDate = SimpleDateFormat("EEEE, d 'DE' MMMM 'DE' yyyy", Locale("es", "ES")).format(Date()).uppercase(Locale.getDefault())

    private val _displayedMonth = MutableStateFlow(Calendar.getInstance())
    val displayedMonth: StateFlow<Calendar> = _displayedMonth

    private val _selectedDate = MutableStateFlow(Calendar.getInstance())
    val selectedDate: StateFlow<Calendar> = _selectedDate

    fun changeMonth(offset: Int) {
        val newMonth = _displayedMonth.value.clone() as Calendar
        newMonth.add(Calendar.MONTH, offset)
        _displayedMonth.value = newMonth
    }

    fun selectDate(day: Int) {
        val newDate = _displayedMonth.value.clone() as Calendar
        newDate.set(Calendar.DAY_OF_MONTH, day)
        _selectedDate.value = newDate
    }

    init { loadUserData() }

    fun onNameChange(newName: String) {
        if (newName.all { it.isLetter() || it.isWhitespace() }) _userName.value = newName
    }
    fun onWeightChange(newWeight: String) { _weight.value = newWeight }
    fun onDateSelected(millis: Long?) { _pregnancyDateMillis.value = millis }

    fun saveAllData(onNavigate: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            userPreferences.saveUserData(_userName.value, _weight.value, formattedPregnancyDate)
            loadUserData()
            withContext(Dispatchers.Main) { onNavigate() }
        }
    }

    private fun loadUserData() {
        viewModelScope.launch {
            val prefs = getApplication<Application>().dataStore.data.first()
            _userName.value = prefs[UserPreferences.USER_NAME_KEY] ?: ""
            _weight.value = prefs[UserPreferences.USER_WEIGHT_KEY] ?: ""
            val dateStr = prefs[UserPreferences.PREGNANCY_DATE_KEY] ?: ""
            if (dateStr.isNotEmpty()) calculatePregnancyData(dateStr)
        }
    }

    private fun calculatePregnancyData(conceptionDateStr: String) {
        try {
            val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val conceptionDate = sdf.parse(conceptionDateStr) ?: return
            val today = Date()

            val diffInMillies = today.time - conceptionDate.time
            val daysPassed = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS).toInt()

            currentWeek.value = ((daysPassed / 7) + 1).coerceIn(1, 40)

            val calendar = Calendar.getInstance()
            calendar.time = conceptionDate
            calendar.add(Calendar.DAY_OF_YEAR, 266)
            dueDate.value = sdf.format(calendar.time)
            daysRemaining.value = (266 - daysPassed).coerceAtLeast(0)
        } catch (e: Exception) { e.printStackTrace() }
    }

    fun getBabySizeInfo(week: Int): BabySize {
        return when (week) {
            in 1..4 -> BabySize("Semilla de Manzana", "4mm", "1g")
            in 5..8 -> BabySize("Uva", "1.6cm", "1.2g")
            in 9..12 -> BabySize("Kiwi", "10cm", "50g")
            in 13..16 -> BabySize("Aguacate", "11.5cm", "100g")
            in 17..20 -> BabySize("Papaya", "16cm", "320g")
            in 21..24 -> BabySize("Mazorca de Maíz", "21cm", "600g")
            in 25..28 -> BabySize("Berenjena", "30cm", "1kg")
            in 29..32 -> BabySize("Piña", "42cm", "1.7kg")
            else -> BabySize("Calabaza", "45cm", "3-4kg")
        }
    }
}

data class BabySize(val name: String, val length: String, val weight: String)