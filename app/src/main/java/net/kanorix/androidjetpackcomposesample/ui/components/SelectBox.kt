package net.kanorix.androidjetpackcomposesample.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun SelectBox(
    options: List<String>,
    selectedOption: String,
    modifier: Modifier = Modifier,
    onOptionSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val icon = if (expanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown

    Box(modifier.padding(5.dp)) {
        Box(
            modifier
                .padding(horizontal = 16.dp)
                .border(width = 1.dp, color = Color.DarkGray, shape = RoundedCornerShape(20.dp))
        ) {
            Text(
                text = selectedOption,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = true }
                    .padding(16.dp)
            )
            Icon(
                icon,
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 16.dp)
            )
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = modifier
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(text = option) },
                        onClick = {
                            onOptionSelected(option)
                            expanded = false
                        })
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SelectBoxPreview() {
    val options = listOf("オプション1", "オプション2", "オプション3")
    var selectedOption by remember { mutableStateOf(options.first()) }
    SelectBox(
        options,
        selectedOption,
        modifier = Modifier.width(300.dp)
    ) { option ->
        // セレクトボックスで選択されたオプションを更新
        selectedOption = option
    }
}