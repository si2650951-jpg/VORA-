package com.example.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.GlassCard
import com.example.ui.navigation.Screen
import com.example.ui.theme.*
import com.example.ui.viewmodel.VoraViewModel

@Composable
fun SignInScreen(
    viewModel: VoraViewModel,
    onNavigate: (String) -> Unit
) {
    var emailOrUser by remember { mutableStateOf("superadmin") }
    var password by remember { mutableStateOf("admin") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkNavyBackground)
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(72.dp)
                .clip(CircleShape)
                .background(Brush.linearGradient(listOf(VoraPurple, VoraCyan))),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.GraphicEq, contentDescription = "Logo", tint = Color.White, modifier = Modifier.size(40.dp))
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text("VORA VOICE", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
        Text("Social & Voice Communication Hub", fontSize = 13.sp, color = TextSecondary)

        Spacer(modifier = Modifier.height(32.dp))

        GlassCard(modifier = Modifier.fillMaxWidth()) {
            Text("Sign In", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = emailOrUser,
                onValueChange = { emailOrUser = it },
                label = { Text("Email or Username") },
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = VoraPurpleLight) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = VoraPurple,
                    unfocusedBorderColor = GlassBorder,
                    focusedLabelColor = VoraPurpleLight
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = VoraPurpleLight) },
                visualTransformation = PasswordVisualTransformation(),
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = VoraPurple,
                    unfocusedBorderColor = GlassBorder,
                    focusedLabelColor = VoraPurpleLight
                )
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    viewModel.login(emailOrUser, password) {
                        onNavigate(Screen.Home.route)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = VoraPurple)
            ) {
                Text("Sign In", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                "Social Login Options",
                fontSize = 12.sp,
                color = TextMuted,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                SocialButton("Gmail", Icons.Default.Email, VoraRose) { onNavigate(Screen.GmailLogin.route) }
                SocialButton("Facebook", Icons.Default.ThumbUp, VoraBlue) { onNavigate(Screen.FacebookLogin.route) }
                SocialButton("Mobile", Icons.Default.Phone, VoraEmerald) { onNavigate(Screen.MobileLogin.route) }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Don't have an account? ", color = TextSecondary, fontSize = 14.sp)
            Text(
                "Sign Up",
                color = VoraCyan,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                modifier = Modifier.clickable { onNavigate(Screen.SignUp.route) }
            )
        }

        Spacer(modifier = Modifier.height(12.dp))
        Text(
            "Admin Login",
            color = VoraGold,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.clickable { onNavigate(Screen.AdminLogin.route) }
        )
    }
}

@Composable
private fun SocialButton(label: String, icon: androidx.compose.ui.graphics.vector.ImageVector, color: Color, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(color.copy(alpha = 0.2f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(imageVector = icon, contentDescription = label, tint = color, modifier = Modifier.size(22.dp))
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = label, fontSize = 11.sp, color = TextSecondary)
    }
}

@Composable
fun SignUpScreen(
    viewModel: VoraViewModel,
    onNavigate: (String) -> Unit
) {
    var fullName by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var mobile by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var country by remember { mutableStateOf("United States") }
    var gender by remember { mutableStateOf("Male") }
    var bio by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkNavyBackground)
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Create VORA Account", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
        Text("Join thousands in live audio & social chats", fontSize = 13.sp, color = TextSecondary)

        Spacer(modifier = Modifier.height(24.dp))

        GlassCard(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = fullName, onValueChange = { fullName = it },
                label = { Text("Full Name") }, modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = username, onValueChange = { username = it },
                label = { Text("Username") }, modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = email, onValueChange = { email = it },
                label = { Text("Email Address") }, modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = mobile, onValueChange = { mobile = it },
                label = { Text("Mobile Number") }, modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = password, onValueChange = { password = it },
                label = { Text("Password") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = country, onValueChange = { country = it },
                label = { Text("Country") }, modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = bio, onValueChange = { bio = it },
                label = { Text("Short Bio") }, modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    viewModel.register(fullName, username, email, mobile, password, country, gender, bio) {
                        onNavigate(Screen.Home.route)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = VoraPurple)
            ) {
                Text("Complete Registration", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }
        }
    }
}

@Composable
fun SocialLoginScreen(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    brandColor: Color,
    viewModel: VoraViewModel,
    onNavigate: (String) -> Unit
) {
    var identifier by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkNavyBackground)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .background(brandColor.copy(alpha = 0.2f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(imageVector = icon, contentDescription = title, tint = brandColor, modifier = Modifier.size(44.dp))
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text(title, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
        Text("Authenticate securely via $title", fontSize = 13.sp, color = TextSecondary)

        Spacer(modifier = Modifier.height(28.dp))

        GlassCard(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = identifier,
                onValueChange = { identifier = it },
                label = { Text("Enter $title Account ID / Email") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    viewModel.loginWithMobile(if (identifier.isEmpty()) "+1234567890" else identifier) {
                        onNavigate(Screen.Home.route)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = brandColor)
            ) {
                Text("Continue with $title", fontWeight = FontWeight.Bold, color = Color.White)
            }
        }
    }
}

@Composable
fun MobileLoginScreen(
    viewModel: VoraViewModel,
    onNavigate: (String) -> Unit
) {
    var mobileNumber by remember { mutableStateOf("+1 555 019 2834") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkNavyBackground)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Mobile Login", fontSize = 26.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
        Text("We'll send you an OTP SMS code", fontSize = 13.sp, color = TextSecondary)

        Spacer(modifier = Modifier.height(24.dp))

        GlassCard(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = mobileNumber,
                onValueChange = { mobileNumber = it },
                label = { Text("Mobile Phone Number") },
                leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null, tint = VoraEmerald) },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = { onNavigate(Screen.OtpVerification.route) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = VoraEmerald)
            ) {
                Text("Send Verification OTP", fontWeight = FontWeight.Bold, color = Color.White)
            }
        }
    }
}

@Composable
fun OtpScreen(
    viewModel: VoraViewModel,
    onNavigate: (String) -> Unit
) {
    var otpCode by remember { mutableStateOf("8492") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkNavyBackground)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("OTP Verification", fontSize = 26.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
        Text("Enter the 4-digit code sent to your phone", fontSize = 13.sp, color = TextSecondary)

        Spacer(modifier = Modifier.height(24.dp))

        GlassCard(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = otpCode,
                onValueChange = { otpCode = it },
                label = { Text("4-Digit OTP Code") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    viewModel.loginWithMobile("+15550192834") {
                        onNavigate(Screen.Home.route)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = VoraPurple)
            ) {
                Text("Verify & Sign In", fontWeight = FontWeight.Bold, color = Color.White)
            }
        }
    }
}

@Composable
fun AdminLoginScreen(
    viewModel: VoraViewModel,
    onNavigate: (String) -> Unit
) {
    var adminUser by remember { mutableStateOf("superadmin") }
    var adminPass by remember { mutableStateOf("admin") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkNavyBackground)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(Icons.Default.AdminPanelSettings, contentDescription = "Admin", tint = VoraGold, modifier = Modifier.size(64.dp))
        Spacer(modifier = Modifier.height(12.dp))
        Text("VORA Admin Portal", fontSize = 26.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
        Text("Secure Role-Based Access Panel", fontSize = 13.sp, color = TextSecondary)

        Spacer(modifier = Modifier.height(28.dp))

        GlassCard(
            modifier = Modifier.fillMaxWidth(),
            borderColor = VoraGold.copy(alpha = 0.5f)
        ) {
            OutlinedTextField(
                value = adminUser,
                onValueChange = { adminUser = it },
                label = { Text("Admin Account") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = adminPass,
                onValueChange = { adminPass = it },
                label = { Text("Secret Key") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    viewModel.login(adminUser, adminPass) {
                        viewModel.switchUserRole("SUPER_ADMIN")
                        onNavigate(Screen.AdminDashboard.route)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = VoraGold)
            ) {
                Text("Access Admin Panel 🔐", fontWeight = FontWeight.Bold, color = Color.Black)
            }
        }
    }
}
