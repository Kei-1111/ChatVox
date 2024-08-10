package com.example.chatvox.widget

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.DpSize
import androidx.glance.ExperimentalGlanceApi
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.LocalContext
import androidx.glance.LocalSize
import androidx.glance.action.Action
import androidx.glance.action.ActionParameters
import androidx.glance.action.actionParametersOf
import androidx.glance.action.actionStartActivity
import androidx.glance.action.clickable
import androidx.glance.appwidget.cornerRadius
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.padding
import androidx.glance.layout.size
import com.example.chatvox.MainActivity
import com.example.chatvox.R
import com.example.chatvox.data.VoicevoxDataStore
import com.example.chatvox.model.Voicevox
import com.example.chatvox.ui.navigation.Screens
import com.example.chatvox.widget.ChatvoxWidget.Companion.LARGE_SQUARE
import com.example.chatvox.widget.ChatvoxWidget.Companion.SMALL_SQUARE

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ChatvoxWidgetContent(
    isDynamicColor: Boolean,
    index: Int,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit
) {
    val context = LocalContext.current
    val currentSize = LocalSize.current

    val startChat = actionStartActivity<MainActivity>(
        actionParametersOf(
            ActionParameters.Key<String>(Screens.KEY_DESTINATION) to "${Screens.Chat.route}/${VoicevoxDataStore.list[index].voicevoxType.name}"
        )
    )

    WidgetTheme(
        dynamicColor = isDynamicColor,
    ) {
        when (currentSize) {
            SMALL_SQUARE -> {
                ChatvoxWidgetContentSmall(
                    size = SMALL_SQUARE,
                    currentVoicevox = VoicevoxDataStore.list[index],
                    startChatAction = startChat
                )
            }

            LARGE_SQUARE -> {
                ChatvoxWidgetContentLarge(
                    context = context,
                    size = LARGE_SQUARE,
                    currentVoicevox = VoicevoxDataStore.list[index],
                    onDecrease = onDecrease,
                    onIncrease = onIncrease,
                    startChatAction = startChat
                )
            }
        }
    }
}

@OptIn(ExperimentalGlanceApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ChatvoxWidgetContentLarge(
    context: Context,
    size: DpSize,
    currentVoicevox: Voicevox,
    onDecrease: () -> Unit,
    onIncrease: () -> Unit,
    startChatAction: Action
) {
    Box(
        modifier = GlanceModifier
            .size(size.width, size.height)
            .padding(glanceDimensionResource(id = R.dimen.medium_padding))
            .background(colorProvider = GlanceTheme.colors.surface),
        contentAlignment = Alignment.Center
    ) {
        Image(
            provider = ImageProvider(currentVoicevox.icon),
            contentDescription = glanceStringResource(id = R.string.voicevox_image_description),
            modifier = GlanceModifier
                .cornerRadius(glanceDimensionResource(id = R.dimen.widget_corner_radius))
                .background(colorProvider = GlanceTheme.colors.surfaceVariant)
                .clickable(
                    onClick = startChatAction
                )
        )
        Row(
            modifier = GlanceModifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            GlanceIcon(
                modifier = GlanceModifier
                    .clickable(
                        key = "decrement",
                        block = onDecrease
                    ),
                iconRes = R.drawable.ic_previous,
                contentDescription = glanceStringResource(id = R.string.previous_icon_description),
                color = GlanceTheme.colors.primary.getColor(context),
            )
            Spacer(modifier = GlanceModifier.defaultWeight())
            GlanceIcon(
                modifier = GlanceModifier
                    .clickable(
                        key = "increment",
                        block = onIncrease
                    ),
                iconRes = R.drawable.ic_next,
                contentDescription = glanceStringResource(id = R.string.next_icon_description),
                color = GlanceTheme.colors.primary.getColor(context),
            )
        }
    }
}

@Composable
fun ChatvoxWidgetContentSmall(
    size: DpSize,
    currentVoicevox: Voicevox,
    startChatAction: Action
) {
    Box(
        modifier = GlanceModifier
            .size(size.width, size.height)
            .padding(glanceDimensionResource(id = R.dimen.medium_padding))
            .background(colorProvider = GlanceTheme.colors.surface),
        contentAlignment = Alignment.Center
    ) {
        Image(
            provider = ImageProvider(currentVoicevox.icon),
            contentDescription = glanceStringResource(id = R.string.voicevox_image_description),
            modifier = GlanceModifier
                .cornerRadius(glanceDimensionResource(id = R.dimen.widget_corner_radius))
                .background(colorProvider = GlanceTheme.colors.surfaceVariant)
                .clickable(
                    onClick = startChatAction
                )
        )
    }
}