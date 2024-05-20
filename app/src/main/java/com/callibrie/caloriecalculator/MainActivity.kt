package com.callibrie.caloriecalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.callibrie.caloriecalculator.models.PFCStrings
import com.callibrie.caloriecalculator.ui.theme.CalorieCalculatorTheme

class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CalorieCalculatorTheme {
                val pfcState = viewModel.pfcState.collectAsState()
                Content(
                    intake = pfcState.value,
                    displayStrings = viewModel.getSummaryStrings(),
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
    intake: List<PFCStrings>,
    displayStrings: PFCStrings,
    onIntakeUpdate: (List<PFCStrings>) -> Unit,
) {
    Column(
        modifier
            .verticalScroll(rememberScrollState())
            .border(width = 2.dp, color = Color.DarkGray)
    ) {
        Header()
        Display(displayStrings)
        ExpandableInputs(intake, onIntakeUpdate)
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
fun Display(uiStrings: PFCStrings) {
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
        uiStrings.protein?.let {
            ViewItem(
                icon = Icons.Default.Star,
                label = "Protein : ",
                value = it
            )
        }
        uiStrings.fat?.let {
            ViewItem(
                icon = Icons.Default.Favorite,
                label = "Fat : ",
                value = it
            )
        }
        uiStrings.carbs?.let {
            ViewItem(
                icon = Icons.Default.PlayArrow,
                label = "Carbohydrates : ",
                value = it
            )
        }
        uiStrings.total?.let {
            Divider()
            ViewItem(
                icon = Icons.Default.Person,
                label = "Total Calories : ",
                value = it
            )
        }
    }
}

@Composable
fun ExpandableInputs(
    intakeList: List<PFCStrings>,
    onIntakeUpdate: (List<PFCStrings>) -> Unit
) {

    val currentValues = remember { intakeList.toMutableStateList() }

    Button(
        modifier = Modifier.padding(16.dp),
        onClick = { currentValues.add(PFCStrings()) }
    ) { Text("Add Food Component") }

    currentValues.forEachIndexed { index, values ->

        Text(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
            text = "Food item ${index + 1}"
        )
        Form(values = values) { newValues ->
            currentValues[index] = newValues
        }
        Divider()
    }

    Submit { onIntakeUpdate(currentValues) }
}

@Composable
fun Form(values: PFCStrings, onValueUpdate: (PFCStrings) -> Unit) {

    Column(
        Modifier
            .fillMaxWidth()
            .background(Color.LightGray.copy(alpha = 0.2f))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OutlinedTextField(
            value = values.name.orEmpty(),
            label = { Text(text = "Food name") },
            onValueChange = { newName ->
                println("CAM | name : $newName")
                onValueUpdate(values.copy(name = newName))
            })

        // Protein(たんぱく質)
        OutlinedTextField(
            value = values.protein.orEmpty(),
            label = { Text(text = "Protein (grams)") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal,
                imeAction = ImeAction.Next
            ),
            onValueChange = { newValue ->
                onValueUpdate(values.copy(protein = newValue))
            }
        )

        // Fat(脂質)
        OutlinedTextField(
            value = values.fat.orEmpty(),
            label = { Text(text = "Fat (grams)") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal,
                imeAction = ImeAction.Next
            ),
            onValueChange = { newValue ->
                onValueUpdate(values.copy(fat = newValue))
            }
        )

        // Carbohydrates(炭水化物)
        OutlinedTextField(
            value = values.carbs.orEmpty(),
            label = { Text(text = "Carbohydrates (grams)") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal,
                imeAction = ImeAction.Done
            ),
            onValueChange = { newValue ->
                onValueUpdate(values.copy(carbs = newValue))
            }
        )
    }
}

@Composable
fun Submit(onButtonClick: () -> Unit) {
    Button(
        modifier = Modifier.padding(16.dp),
        onClick = onButtonClick
    ) {
        Text(text = "Calculate")
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
            intake = listOf(PFCStrings("Foodums", "10.0", "20", "30")),
            displayStrings = PFCStrings(protein = "100", fat = "200", carbs = "300", total = "600"),
            onIntakeUpdate = {}
        )
    }
}