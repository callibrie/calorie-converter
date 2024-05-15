package com.callibrie.caloriecalculator

import com.callibrie.caloriecalculator.models.Nutrient
import com.callibrie.caloriecalculator.models.PFCIntake
import junit.framework.TestCase.assertEquals
import org.junit.Test

class PFCConversionTests {
    private val testPFC = PFCIntake(
        protein = 1.0f,
        fat = 2.0f,
        carbohydrates = 3.0f
    )

    private val expectedCalories = mapOf(
        Nutrient.Protein to 4, // 1.0g protein * 4 kcal
        Nutrient.Fat to 18, // 2.0 fat * 9 kcal
        Nutrient.Carbohydrates to 12 // 3.0 carbohydrates * 4 kcal
    )

    @Test
    fun `Protein intake (g) conversion to Energy (kcal) is accurate`() {
        val expectedValue = expectedCalories[Nutrient.Protein]
        assertEquals(expectedValue, testPFC.proteinToEnergy())
    }

    @Test
    fun `Fat intake (g) conversion to Energy (kcal) is accurate`() {
        val expectedValue = expectedCalories[Nutrient.Fat]
        assertEquals(expectedValue, testPFC.fatToEnergy())
    }

    @Test
    fun `Carbohydrate intake (g) conversion to Energy (kcal) is accurate`() {
        val expectedValue = expectedCalories[Nutrient.Carbohydrates]
        assertEquals(expectedValue, testPFC.carbsToEnergy())
    }

    @Test
    fun `Total PFC value computation in accurate`() {
        val expectedValue = expectedCalories.values.sum()
        assertEquals(expectedValue, testPFC.totalCalories)
    }
}
