package com.example.menlovending.ui.widgets

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenloVendingScaffold(title: String = "", content: @Composable (PaddingValues) -> Unit) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    if (title.isNotEmpty()) {
                        Text(title, style = MaterialTheme.typography.headlineMedium)
                    } else {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Menlo Vending", style = MaterialTheme.typography.titleLarge)
                            Text("By Cody Kletter", style = MaterialTheme.typography.titleSmall)
                        }
                    }
                }
            )
        },
    ) { innerPadding -> content(innerPadding) }
}