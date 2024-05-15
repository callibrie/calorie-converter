package com.callibrie.caloriecalculator

import com.callibrie.caloriecalculator.models.PFCIntake
import com.callibrie.caloriecalculator.models.PFCStrings
import org.junit.Test

class MainViewModelTests {
    private val vm = MainViewModel()

    private val testValues = PFCStrings(
        proteinInput = "1.000598",
        fatInput = "0.998",
        carbInput = "10.0199"
    )

    @Test
    fun `Updated protein intake values have exactly one decimal place`() {

        vm.updateIntakeValues(
            PFCIntake(
                protein = testValues.proteinInput?.toFloatOrNull() ?: 0f,
                fat = 0f,
                carbohydrates = 0f
            )
        )
        val updatedValues = vm.pfcState.getStringValues()
        assert(updatedValues.proteinInput?.substringAfter(".")?.length == 1)
    }

    @Test
    fun `Updated fat intake values have exactly one decimal place`() {
        vm.updateIntakeValues(
            PFCIntake(
                protein = 0f,
                fat = testValues.fatInput?.toFloatOrNull() ?: 0f,
                carbohydrates = 0f
            )
        )
        val updatedValues = vm.pfcState.getStringValues()
        assert(updatedValues.fatInput?.substringAfter(".")?.length == 1)
    }

    @Test
    fun `Updated carbohydrates intake values have exactly one decimal place`() {

        vm.updateIntakeValues(
            PFCIntake(
                protein = 0f,
                fat = 0f,
                carbohydrates = testValues.carbInput?.toFloatOrNull() ?: 0f
            )
        )
        val updatedValues = vm.pfcState.getStringValues()
        assert(updatedValues.carbInput?.substringAfter(".")?.length == 1)
    }
}