package net.kanorix.androidjetpackcomposesample.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

data class RadioGroupData<T>(
    val title: String,
    val value: T,
)

@Composable
fun <T> RadioGroup(
    elements: List<RadioGroupData<T>>,
    selectedOption: T,
    onOptionSelected: (T) -> Unit
) {
    Row(Modifier.padding(5.dp)) {
        elements.forEach { it ->
            Row(verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(horizontal = 5.dp)
                    .selectable(
                        selected = selectedOption == it.value,
                        onClick = {
                            onOptionSelected(it.value)
                        }
                    )) {
                RadioButton(
                    selected = selectedOption == it.value,
                    onClick = { onOptionSelected(it.value) })
                Text(it.title)
            }
        }
    }
}