package com.callibrie.caloriecalculator.models

import com.callibrie.caloriecalculator.utility.toNonZeroString
import kotlin.math.roundToInt

/**
 * Food Protein, Fat, Carbohydrate (PFC) breakdown in grams
 * Conversion to Calories facilitated by [Nutrient] values
 *
 * Rules:
 * 1) Intake values are rounded to 1 decimal place
 * 2) Calorie values are rounded to an integer
 *
 * 「三大栄養素」（PFC）で食べ物を分けます。グラムで計算します。
 *
 *
 */
data class PFCIntake(
    val protein: Float,
    val fat: Float,
    val carbohydrates: Float
) {
    fun proteinToEnergy(): Int {
        val calories = this.protein * Nutrient.Protein.calories
        return calories.roundToInt()
    }

    fun fatToEnergy(): Int {
        val calories = this.fat * Nutrient.Fat.calories
        return calories.roundToInt()
    }

    fun carbsToEnergy(): Int {
        val calories = this.carbohydrates * Nutrient.Carbohydrates.calories
        return calories.roundToInt()
    }

    fun getStringValues(): PFCStrings {
        return PFCStrings(
            proteinInput = this.protein.toNonZeroString(),
            fatInput = this.fat.toNonZeroString(),
            carbInput = this.carbohydrates.toNonZeroString()
        )
    }

    val totalCalories = arrayOf(
        proteinToEnergy(),
        fatToEnergy(),
        carbsToEnergy()
    ).sum()
}

/**
 * Calorie conversion values per gram
 */
enum class Nutrient(val calories: Float) {
    Protein(4f),
    Fat(9f),
    Carbohydrates(4f)
}

/**
 * String class for UI
 */
data class PFCStrings(
    val proteinInput: String?,
    val fatInput: String?,
    val carbInput: String?
)
