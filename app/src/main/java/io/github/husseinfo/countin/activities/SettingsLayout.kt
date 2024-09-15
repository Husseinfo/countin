package io.github.husseinfo.countin.activities

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.husseinfo.countin.R
import io.github.husseinfo.countin.data.AppDatabase
import io.github.husseinfo.countin.theme.AppTheme


@Composable
fun SettingsUI(onImportClick: () -> Unit = {}) {
    val context = LocalContext.current
    AppTheme {
        Column {
            Button(
                onClick = { onImportClick() },
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
            )
            {
                Text(stringResource(id = R.string._import))
            }
            Button(
                onClick = {
                    AppDatabase.getDb(context)?.export(context)
                },
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
            )
            {
                Text(stringResource(id = R.string.export))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsPreview() {
    SettingsUI()
}
