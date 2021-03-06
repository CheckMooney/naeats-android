package com.checkmooney.naeats.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TopBar(title: String) {
    Surface(
        contentColor = Color.Black,
        color = MaterialTheme.colors.background,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = title,
            modifier = Modifier.padding(start = 8.dp, end = 8.dp, top = 80.dp, bottom = 20.dp),
            fontSize = 25.sp,
        )
    }
}

@Preview
@Composable
fun TopBarPreview() {
    TopBar(title = "테스트")
}
