package com.callibrie.caloriecalculator

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.callibrie.caloriecalculator.models.Nutrient
import com.callibrie.caloriecalculator.models.PFCIntake
import com.callibrie.caloriecalculator.models.PFCStrings
import com.callibrie.caloriecalculator.utility.roundToOneDecimal
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class MainViewModel : ViewModel() {

    @VisibleForTesting
    private val pfcArray = MutableStateFlow(
        arrayOf(PFCIntake("", 0f, 0f, 0f))
    )

    // UI State for display
    val pfcState = pfcArray.map { value ->
        value.map { it.getStringValues() }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), listOf(PFCStrings()))

    fun updateIntakeValues(userInputs: List<PFCStrings>) {
        val intakeList = mutableListOf<PFCIntake>()
        userInputs.filter { it.containsValue() }.forEach { input ->
            val intake = PFCIntake(
                name = input.name.orEmpty(),
                protein = input.protein.toFloatOrZero().roundToOneDecimal(),
                fat = input.fat.toFloatOrZero().roundToOneDecimal(),
                carbohydrates = input.carbs.toFloatOrZero().roundToOneDecimal()
            )
            intakeList.add(intake)
        }
        // Complete replacement of the state based on input
        pfcArray.tryEmit(intakeList.toTypedArray())
    }

    // Strings for summary of all food calories
    fun getSummaryStrings(): PFCStrings {
        val valueMap = getTotalPerNutrient()
        val total = valueMap.values.sum()

        return PFCStrings(
            protein = valueMap[Nutrient.Protein].toString(),
            fat = valueMap[Nutrient.Fat].toString(),
            carbs = valueMap[Nutrient.Carbohydrates].toString(),
            total = total.toString()
        )

    }

    private fun getTotalPerNutrient(): Map<Nutrient, Int> {
        val valueMap = emptyMap<Nutrient, Int>().toMutableMap()

        pfcArray.value.forEach { intake ->
            val protein = valueMap.getOrDefault(Nutrient.Protein, 0) + intake.proteinToEnergy()
            valueMap[Nutrient.Protein] = protein

            val fat = valueMap.getOrDefault(Nutrient.Fat, 0) + intake.fatToEnergy()
            valueMap[Nutrient.Fat] = fat

            val carbohydrates =
                valueMap.getOrDefault(Nutrient.Carbohydrates, 0) + intake.carbsToEnergy()
            valueMap[Nutrient.Carbohydrates] = carbohydrates
        }
        return valueMap
    }

    private fun String?.toFloatOrZero(): Float {
        return this?.toFloatOrNull() ?: 0f
    }

}
