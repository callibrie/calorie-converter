package com.callibrie.caloriecalculator

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.callibrie.caloriecalculator.models.PFCIntake
import com.callibrie.caloriecalculator.utility.roundToOneDecimal

class MainViewModel : ViewModel() {

    var pfcState by mutableStateOf(
        PFCIntake(0f, 0f, 0f)
    )

    fun updateIntakeValues(userPFCIntake: PFCIntake) {
        val (uProtein, uFat, uCarbs) = userPFCIntake.apply {
            this.protein.roundToOneDecimal()
        }
        pfcState = PFCIntake(
            protein = uProtein.roundToOneDecimal(),
            fat = uFat.roundToOneDecimal(),
            carbohydrates = uCarbs.roundToOneDecimal()
        )
    }
}
