package com.example.chatvox.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.window.Dialog
import com.example.chatvox.R

@Composable
fun ChatVoxDialog(
    oldUserName: String,
    onRegister: (String) -> Unit,
    onDismissRequest: () -> Unit = {}
) {
    var newUserName by remember { mutableStateOf(oldUserName) }

    Dialog(
        onDismissRequest = onDismissRequest,
    ) {
        Card {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = dimensionResource(id = R.dimen.extra_large_padding),
                        vertical = dimensionResource(id = R.dimen.extra_large_padding)
                    ),
                verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.extra_large_padding))
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.extra_small_padding))
                ) {
                    Text(
                        text = "ユーザー名の登録",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "Voicevoxから呼ばれるユーザー名を登録してください。",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                BasicTextField(
                    value = newUserName,
                    onValueChange = { newUserName = it },
                    singleLine = true,
                    maxLines = 1,
                    textStyle = MaterialTheme.typography.bodyLarge.copy(
                        color = MaterialTheme.colorScheme.onSurface
                    ),
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(
                        onDone = {}
                    ),
                    cursorBrush = SolidColor(MaterialTheme.colorScheme.primary)
                ){ innerTextField ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(dimensionResource(id = R.dimen.tiny_padding)),
                        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.tiny_padding))
                    ) {
                        innerTextField()
                        HorizontalDivider(
                            modifier = Modifier.fillMaxWidth(),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
                Text(
                    text = "登録",
                    modifier = Modifier
                        .align(Alignment.End)
                        .clickable(
                            enabled = newUserName.isNotEmpty(),
                            onClick = {
                                onRegister(newUserName)
                            }
                        ),
                    style = MaterialTheme.typography.titleMedium,
                    color = if(newUserName.isNotEmpty()) MaterialTheme.colorScheme.primary else Color.Gray
                )
            }
        }
    }
}