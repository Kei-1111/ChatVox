package com.example.chatvox.widget

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.LocalContext
import androidx.glance.action.ActionParameters
import androidx.glance.action.actionParametersOf
import androidx.glance.action.actionStartActivity
import androidx.glance.action.clickable
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.cornerRadius
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.padding
import androidx.glance.layout.size
import com.example.chatvox.MainActivity
import com.example.chatvox.R
import com.example.chatvox.data.AppPreferencesRepository
import com.example.chatvox.data.VoicevoxDataStore
import com.example.chatvox.model.AppSettings
import com.example.chatvox.ui.navigation.Screens

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ChatvoxWidgetContent(
    size: Dp,
    appPreferencesRepository: AppPreferencesRepository,
) {
    var appSettings by remember { mutableStateOf(AppSettings()) }
    var count by remember { mutableStateOf(0) }

    val destinationKey = ActionParameters.Key<String>(Screens.KEY_DESTINATION)

    val indexKey = ActionParameters.Key<Int>(UpdateIndexAction.WIDGET_CURRENT_VOICEVOX_INDEX)

    val context = LocalContext.current


    LaunchedEffect(Unit) {
        appPreferencesRepository.appSettings.collect {
            appSettings = it
        }
    }

    LaunchedEffect(Unit) {
        appPreferencesRepository.widgetCurrentVoicevoxIndex.collect {
            count = it
        }
    }

    WidgetTheme(
        dynamicColor = appSettings.isDynamicColor
    ) {
        Box(
            modifier = GlanceModifier
                .size(size)
                .background(colorProvider = GlanceTheme.colors.surface),
            contentAlignment = Alignment.Center
        ) {
            if (size > 100.dp) {
                Row(
                    modifier = GlanceModifier.fillMaxWidth()
                ) {
                    GlanceIcon(
                        modifier = GlanceModifier
                            .size(size / 5)
                            .clickable(
                                onClick = actionRunCallback<UpdateIndexAction>(
                                    parameters = actionParametersOf(
                                        indexKey to (count - 1).let { if (it < 0) VoicevoxDataStore.list.size - 1 else it }
                                    )
                                )
                            ),
                        iconRes = R.drawable.ic_previous,
                        contentDescription = "previous",
                        color = GlanceTheme.colors.primary.getColor(context),
                    )
                    Spacer(
                        modifier = GlanceModifier.defaultWeight()
                    )
                    GlanceIcon(
                        modifier = GlanceModifier
                            .size(size / 5)
                            .clickable(
                                onClick = actionRunCallback<UpdateIndexAction>(
                                    parameters = actionParametersOf(
                                        indexKey to (count + 1).let { if (it > VoicevoxDataStore.list.size - 1) 0 else it }
                                    )
                                )
                            ),
                        iconRes = R.drawable.ic_next,
                        contentDescription = "next",
                        color = GlanceTheme.colors.primary.getColor(context),
                    )
                }
            }
            Box(
                modifier = GlanceModifier
                    .fillMaxSize()
                    .padding(size / 6)
            ) {
                Image(
                    provider = ImageProvider(VoicevoxDataStore.list[count].icon),
                    contentDescription = "Voicevox",
                    modifier = GlanceModifier
                        .cornerRadius(size)
                        .background(colorProvider = GlanceTheme.colors.surfaceVariant)
                        .clickable(
                            onClick = actionStartActivity<MainActivity>(
                                actionParametersOf(
                                    destinationKey to "${Screens.Chat.route}/${VoicevoxDataStore.list[count].voicevoxType.name}"
                                )
                            )
                        )
                )
            }
        }
    }
}