package com.callibrie.caloriecalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.callibrie.caloriecalculator.models.PFCIntake
import com.callibrie.caloriecalculator.models.PFCStrings
import com.callibrie.caloriecalculator.ui.theme.CalorieCalculatorTheme

class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CalorieCalculatorTheme {
                Content(
                    intake = viewModel.pfcState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    onIntakeUpdate = viewModel::updateIntakeValues
                )
            }
        }
    }
}

@Composable
fun Content(
    modifier: Modifier = Modifier,
    intake: PFCIntake,
    onIntakeUpdate: (PFCIntake) -> Unit
) {
    Column(modifier.border(width = 2.dp, color = Color.DarkGray)) {
        Header()
        Display(intake)
        Form(intake.getStringValues(), onIntakeUpdate)
    }
}

@Composable
fun Header() {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.DarkGray)
            .padding(16.dp),
        text = "Calorie Calculator",
        color = Color.White,
        fontSize = 24.sp,
    )
}

//「PFC」のカロリの価値を表示する
@Composable
fun Display(intake: PFCIntake) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Calories From: ",
            fontWeight = FontWeight.Bold
        )
        ViewItem(
            icon = Icons.Default.Star,
            label = "Protein : ",
            value = intake.proteinToEnergy().toString()
        )
        ViewItem(
            icon = Icons.Default.Favorite,
            label = "Fat : ",
            value = intake.fatToEnergy().toString()
        )
        ViewItem(
            icon = Icons.Default.PlayArrow,
            label = "Carbohydrates : ",
            value = intake.carbsToEnergy().toString()
        )
        Divider()
        ViewItem(
            icon = Icons.Default.Person,
            label = "Total Calories : ",
            value = intake.totalCalories.toString()
        )
    }
}

@Composable
fun Form(values: PFCStrings, onIntakeUpdate: (PFCIntake) -> Unit) {
    var inputs by remember(values) { mutableStateOf(values) }

    Column(
        Modifier
            .fillMaxWidth()
            .background(Color.LightGray.copy(alpha = 0.2f))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        // Protein(たんぱく質)
        OutlinedTextField(
            value = inputs.proteinInput.orEmpty(),
            label = { Text(text = "Protein (grams)") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal,
                imeAction = ImeAction.Next
            ),
            onValueChange = { newValue -> inputs = inputs.copy(proteinInput = newValue) }
        )

        // Fat(脂質)
        OutlinedTextField(
            value = inputs.fatInput.orEmpty(),
            label = { Text(text = "Fat (grams)") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal,
                imeAction = ImeAction.Next
            ),
            onValueChange = { newValue -> inputs = inputs.copy(fatInput = newValue) }
        )

        // Carbohydrates(炭水化物)
        OutlinedTextField(
            value = inputs.carbInput.orEmpty(),
            label = { Text(text = "Carbohydrates (grams)") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal,
                imeAction = ImeAction.Done
            ),
            onValueChange = { newValue -> inputs = inputs.copy(carbInput = newValue) }
        )

        // Submit
        Button(onClick = {
            val newValues = PFCIntake(
                protein = inputs.proteinInput?.toFloatOrNull() ?: 0f,
                fat = inputs.fatInput?.toFloatOrNull() ?: 0f,
                carbohydrates = inputs.carbInput?.toFloatOrNull() ?: 0f
            )
            onIntakeUpdate(newValues)
        }) { Text(text = "計算する") }
    }
}

@Composable
fun ViewItem(icon: ImageVector, label: String, value: String) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Icon(imageVector = icon, contentDescription = "")
        Text(text = label, fontWeight = FontWeight.Bold)
        Text(text = "$value kcal")
    }
}

@Preview(showBackground = true)
@Composable
fun ContentPreview() {
    CalorieCalculatorTheme {
        Content(
            modifier = Modifier,
            intake = PFCIntake(10.0f, 20f, 30f),
            onIntakeUpdate = {}
        )
    }
}