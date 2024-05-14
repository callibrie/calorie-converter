package com.callibrie.caloriecalculator

import com.callibrie.caloriecalculator.models.PFCIntake
import junit.framework.TestCase.assertEquals
import org.junit.Test

class PFCConversionTest {
    private val testPFC = PFCIntake(
        protein = 1.0f, // 4 kcal per gram
        fat = 2.0f,     // 9 kcal per gram
        carbohydrates = 3.0f // 4 kcal per gram
    )

    @Test
    fun `Protein intake (g) conversion to Energy (kcal) is accurate`() {
        val expectedValue = 4
        assertEquals(expectedValue, testPFC.proteinToEnergy())
    }

    @Test
    fun `Fat intake (g) conversion to Energy (kcal) is accurate`() {
        val expectedValue = 18
        assertEquals(expectedValue, testPFC.fatToEnergy())
    }

    @Test
    fun `Carbohydrate intake (g) conversion to Energy (kcal) is accurate`() {
        val expectedValue = 12
        assertEquals(expectedValue, testPFC.carbsToEnergy())
    }

    @Test
    fun `Total PFC value computation in accurate`() {
        val expectedValue = arrayOf(
            4, // 1.0g protein * 4 kcal
            18, // 2.0 fat * 9 kcal
            12 // 3.0 carbohydrates * 4 kcal
        ).sum()

        assertEquals(expectedValue, testPFC.totalCalories)
    }
}

