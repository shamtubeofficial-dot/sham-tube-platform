package com.shamtube.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.shamtube.app.R
import com.shamtube.app.viewmodel.AuthViewModel

enum class AuthMode {
    LOGIN,
    REGISTER,
    FORGOT_PASSWORD,
}

@Composable
fun AuthScreen(
    mode: AuthMode,
    viewModel: AuthViewModel,
    onBack: () -> Unit,
    onNavigate: (AuthMode) -> Unit,
    onSuccess: () -> Unit,
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var message by remember { mutableStateOf<String?>(null) }

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = {
                Text(
                    when (mode) {
                        AuthMode.LOGIN -> stringResource(R.string.login)
                        AuthMode.REGISTER -> stringResource(R.string.register)
                        AuthMode.FORGOT_PASSWORD -> stringResource(R.string.forgot_password)
                    },
                )
            },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(Icons.Outlined.ArrowBack, contentDescription = stringResource(R.string.back))
                }
            },
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text("شام تيوب", style = MaterialTheme.typography.headlineLarge, color = MaterialTheme.colorScheme.primary)
            Text(
                stringResource(R.string.auth_local_note),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 8.dp, bottom = 24.dp),
            )
            if (mode == AuthMode.REGISTER) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(stringResource(R.string.name)) },
                    leadingIcon = { Icon(Icons.Outlined.Person, contentDescription = null) },
                    singleLine = true,
                )
                Spacer(modifier = Modifier.height(12.dp))
            }
            if (mode != AuthMode.FORGOT_PASSWORD) {
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(stringResource(R.string.email)) },
                    leadingIcon = { Icon(Icons.Outlined.Email, contentDescription = null) },
                    singleLine = true,
                )
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(stringResource(R.string.password)) },
                    leadingIcon = { Icon(Icons.Outlined.Lock, contentDescription = null) },
                    visualTransformation = PasswordVisualTransformation(),
                    singleLine = true,
                )
            } else {
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(stringResource(R.string.email)) },
                    leadingIcon = { Icon(Icons.Outlined.Email, contentDescription = null) },
                    singleLine = true,
                )
            }
            message?.let {
                Text(
                    it,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 10.dp),
                )
            }
            Button(
                onClick = {
                    when (mode) {
                        AuthMode.LOGIN -> viewModel.login(email, password) {
                            if (it) onSuccess() else message = "تحقق من البريد وكلمة المرور"
                        }
                        AuthMode.REGISTER -> viewModel.register(name, email, password) {
                            if (it) onSuccess() else message = "أكمل بيانات الحساب"
                        }
                        AuthMode.FORGOT_PASSWORD -> message = "تم إرسال رابط الاستعادة إلى بريدك"
                    }
                },
                modifier = Modifier.fillMaxWidth().padding(top = 18.dp).height(52.dp),
            ) {
                Text(
                    when (mode) {
                        AuthMode.LOGIN -> stringResource(R.string.login_action)
                        AuthMode.REGISTER -> stringResource(R.string.register_action)
                        AuthMode.FORGOT_PASSWORD -> stringResource(R.string.send_reset_link)
                    },
                )
            }
            if (mode == AuthMode.LOGIN) {
                TextButton(onClick = { onNavigate(AuthMode.FORGOT_PASSWORD) }) {
                    Text(stringResource(R.string.forgot_password))
                }
                TextButton(onClick = { onNavigate(AuthMode.REGISTER) }) {
                    Text(stringResource(R.string.register))
                }
            } else if (mode == AuthMode.REGISTER) {
                TextButton(onClick = { onNavigate(AuthMode.LOGIN) }) {
                    Text(stringResource(R.string.login))
                }
            } else {
                TextButton(onClick = { onNavigate(AuthMode.LOGIN) }) {
                    Text(stringResource(R.string.login))
                }
            }
        }
    }
}