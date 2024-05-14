package com.callibrie.caloriecalculator.models

import kotlin.math.roundToInt

/**
 * Food Protein, Fat, Carbohydrate (PFC) breakdown in grams
 * Conversion to Calories facilitated by [Nutrient] values
 *
 * Rules:
 * 1) Intake values are rounded to 1 decimal place
 * 2) Calorie values are rounded to an integer
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

