package com.kapirti.social_chat_food_video.common.composable

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.kapirti.social_chat_food_video.R.string as AppText

@Composable
fun BasicField(
    @StringRes text: Int,
    value: String,
    onNewValue: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        singleLine = true,
        modifier = modifier,
        value = value,
        onValueChange = { onNewValue(it) },
        placeholder = { Text(stringResource(text)) }
    )
}

@Composable
fun DescriptionField(
    @StringRes text: Int,
    value: String,
    onNewValue: (String) -> Unit,
    modifier: Modifier = Modifier
){
    OutlinedTextField(
        value = value,
        onValueChange = { onNewValue(it)},
        placeholder = { Text(stringResource(text)) },
        modifier = modifier
            .height(350.dp)
            .fillMaxWidth(),
        colors = TextFieldDefaults.colors(
            focusedTextColor = Color.Blue,
            unfocusedLabelColor = Color.Blue,//Transparent,
            cursorColor = MaterialTheme.colorScheme.secondary
        )
    )
}

@Composable
fun EmailField(
    value: String, onNewValue: (String) -> Unit,
    isError: Boolean,
    modifier: Modifier = Modifier,
) {
    val focusManager = LocalFocusManager.current
    OutlinedTextField(
        singleLine = true,
        modifier = modifier,
        value = value,
        onValueChange = { onNewValue(it) },
        placeholder = { Text(stringResource(AppText.email)) },
        leadingIcon = { Icon(
            imageVector = KapirtiIcons.Email,
            contentDescription = stringResource(
                id = AppText.email,
            ),
        ) },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Next
        ),
        keyboardActions = KeyboardActions(
            onNext = {
                focusManager.moveFocus(
                    FocusDirection.Down
                )
            }
        ),
        isError = isError
    )
}

@Composable
fun PasswordField(
    value: String, onNewValue: (String) -> Unit,
    isError: Boolean, modifier: Modifier = Modifier
) {
    PasswordField(
        value = value, placeholder = AppText.password,
        onNewValue = onNewValue, modifier = modifier,
        isError = isError
    )
}

@Composable
private fun PasswordField(
    value: String,
    @StringRes placeholder: Int,
    isError: Boolean,
    onNewValue: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var isVisible by remember { mutableStateOf(false) }

    val icon =
        if (isVisible) KapirtiIcons.Visibility
        else KapirtiIcons.VisibilityOff

    val visualTransformation =
        if (isVisible) VisualTransformation.None else PasswordVisualTransformation()

    OutlinedTextField(
        modifier = modifier,
        value = value,
        onValueChange = { onNewValue(it) },
        placeholder = { Text(text = stringResource(placeholder)) },
        leadingIcon = {
            Icon(
                imageVector = KapirtiIcons.Lock,
                contentDescription = stringResource(
                    id = AppText.cd_lock,
                ),
            )
        },
        trailingIcon = {
            IconButton(onClick = { isVisible = !isVisible }) {
                Icon(imageVector = icon, contentDescription = stringResource(id = AppText.cd_visibility))
            }
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        visualTransformation = visualTransformation,
        isError = isError
    )
}
