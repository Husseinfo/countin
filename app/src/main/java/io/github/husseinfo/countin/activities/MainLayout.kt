package io.github.husseinfo.countin.activities

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.husseinfo.countin.R
import io.github.husseinfo.countin.data.AppDatabase
import io.github.husseinfo.countin.data.CountModel
import io.github.husseinfo.countin.theme.AppTheme
import io.github.husseinfo.countin.theme.Primary
import io.github.husseinfo.countin.theme.Typography
import io.github.husseinfo.countin.theme.Upcoming
import io.github.husseinfo.maticonsearch.getIcon
import io.github.husseinfo.maticonsearch.getIconByName


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CountsList(
    items: List<CountModel>, updateUI: () -> Unit = {}
) {
    val context = LocalContext.current
    LazyColumn {
        items(items.size) { item ->
            Row(
                Modifier
                    .padding(top = 10.dp, start = 20.dp, end = 20.dp)
                    .combinedClickable(
                        onClick = {
                            val intent = Intent(context, AddItemActivity::class.java)
                            intent.putExtra(EDIT_RECORD_ID, items[item].id)
                            context.startActivity(intent)
                        },
                        onLongClick = {
                            AlertDialog
                                .Builder(context)
                                .setTitle(R.string.confirm_delete)
                                .setPositiveButton(R.string.delete) { _: DialogInterface?, _: Int ->
                                    AppDatabase
                                        .getDb(context)
                                        ?.countDAO()
                                        ?.delete(items[item])
                                    updateUI()
                                }
                                .setNegativeButton(R.string.dismiss) { _: DialogInterface?, _: Int -> }
                                .create()
                                .show()
                        }
                    )
            ) {
                val icon = if (items[item].icon != null)
                    getIconByName(context, items[item].icon!!)
                else
                    getIcon(
                        context,
                        "CalendarMonth",
                        Icons.Filled
                    )
                Icon(
                    modifier = Modifier
                        .size(40.dp)
                        .padding(end = 10.dp),
                    imageVector = icon,
                    tint = if (isSystemInDarkTheme()) Color.LightGray else Color.Black,
                    contentDescription = icon.name
                )
                Column {
                    Text(
                        text = items[item].title,
                        style = Typography.titleSmall,
                        color = if (isSystemInDarkTheme()) Color.LightGray else Color.DarkGray
                    )
                    Text(
                        text = items[item].getPeriod(),
                        style = Typography.bodySmall,
                        color = if (items[item].dateDiff().isNegative) Upcoming else Color.Gray
                    )
                }
                Spacer(modifier = Modifier.weight(1f))

                if (items[item].list != null && items[item].list!!.isNotEmpty())
                    ListShape(item = items[item])
            }
        }
    }
}

@Composable
fun ListShape(item: CountModel) {
    Box(
        modifier = Modifier
            .background(
                color = if (isSystemInDarkTheme()) Color.DarkGray else Color.LightGray,
                shape = RoundedCornerShape(50)
            )
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text = item.list ?: "",
            color = if (isSystemInDarkTheme()) Color.LightGray else Color.DarkGray,
            style = Typography.titleSmall,
        )
    }
}

@Composable
fun Header() {
    val context = LocalContext.current
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_baseline_timeline_primary),
            contentDescription = stringResource(R.string.image),
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .width(200.dp)
                .clickable {
                    context.startActivity(Intent(context, SettingsActivity::class.java))
                }
        )
    }
}


@Composable
fun MainUI(items: List<CountModel>, updateUI: () -> Unit = {}) {
    val context = LocalContext.current
    AppTheme {
        Column {
            Header()
            Box(modifier = Modifier.fillMaxSize()) {
                CountsList(
                    items, updateUI
                )
                FloatingActionButton(
                    onClick = {
                        context.startActivity(
                            Intent(
                                context,
                                AddItemActivity::class.java
                            )
                        )
                    },
                    containerColor = if (isSystemInDarkTheme()) Color.DarkGray else Color.LightGray,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(16.dp),
                ) {
                    Icon(
                        imageVector = Icons.Default.AddCircle,
                        tint = Primary,
                        contentDescription = stringResource(id = R.string.add_item)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainPreview() {
    MainUI(
        listOf(
            CountModel("First", 1703100000000, false, null, "Travel"),
            CountModel("Second", 1672100000000, true, null, null),
            CountModel("Third", 1643102000000, false, null, "Life"),
        )
    )
}
