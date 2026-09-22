package com.example.ui.screens.staticpages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.GlassCard
import com.example.ui.theme.*
import com.example.ui.viewmodel.VoraViewModel

@Composable
fun AboutScreen(onNavigate: (String) -> Unit) {
    StaticPageLayout(title = "About VORA Voice", subtitle = "Voice Chat & Social Communication Platform v2.4.0") {
        Text("VORA Voice is a next-generation social audio ecosystem connecting creators, students, and live audio hosts across the globe.", color = TextPrimary, fontSize = 14.sp)
        Spacer(modifier = Modifier.height(12.dp))
        Text("Features include high-definition voice rooms, 1-on-1 video calls, animated virtual gift exchanges, community photo feeds, and role-based moderation systems.", color = TextSecondary, fontSize = 13.sp)
    }
}

@Composable
fun ContactScreen(viewModel: VoraViewModel, onNavigate: (String) -> Unit) {
    var subject by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }

    StaticPageLayout(title = "Contact Support Team", subtitle = "Send us a direct message or feedback") {
        OutlinedTextField(
            value = subject, onValueChange = { subject = it },
            label = { Text("Subject") }, modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(10.dp))
        OutlinedTextField(
            value = message, onValueChange = { message = it },
            label = { Text("Message details...") }, modifier = Modifier.fillMaxWidth(), minLines = 3
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                viewModel.showToast("Message sent to VORA Support team!")
                subject = ""
                message = ""
            },
            colors = ButtonDefaults.buttonColors(containerColor = VoraPurple),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Send Message", fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun PrivacyPolicyScreen(onNavigate: (String) -> Unit) {
    StaticPageLayout(title = "Privacy Policy", subtitle = "How VORA protects your audio & profile data") {
        Text("1. Data Encryption: All voice streams, chat messages, and user profile metadata are strictly encrypted in transit.", color = TextPrimary, fontSize = 13.sp)
        Spacer(modifier = Modifier.height(8.dp))
        Text("2. Privacy Controls: You control who can call, message, or send you gifts in your User Control Panel.", color = TextSecondary, fontSize = 13.sp)
    }
}

@Composable
fun TermsScreen(onNavigate: (String) -> Unit) {
    StaticPageLayout(title = "Terms & Conditions", subtitle = "User guidelines & community standards") {
        Text("1. Respect Community Guidelines: Harassment, hate speech, and illegal broadcasts in voice rooms are strictly prohibited.", color = TextPrimary, fontSize = 13.sp)
        Spacer(modifier = Modifier.height(8.dp))
        Text("2. Moderation: VORA Voice admins and moderators reserve the right to ban violators.", color = TextSecondary, fontSize = 13.sp)
    }
}

@Composable
fun HelpSupportScreen(onNavigate: (String) -> Unit) {
    StaticPageLayout(title = "Help & FAQ Center", subtitle = "Frequently Asked Questions") {
        Text("Q: How do I earn or buy coins?", fontWeight = FontWeight.Bold, color = VoraGold, fontSize = 14.sp)
        Text("A: Go to Wallet & Coins from the drawer to top-up coin packages.", color = TextSecondary, fontSize = 12.sp)
        Spacer(modifier = Modifier.height(12.dp))
        Text("Q: How do I create a Voice Room?", fontWeight = FontWeight.Bold, color = VoraCyan, fontSize = 14.sp)
        Text("A: Tap 'Rooms' on the bottom bar and click 'Create Room'.", color = TextSecondary, fontSize = 12.sp)
    }
}

@Composable
private fun StaticPageLayout(
    title: String,
    subtitle: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkNavyBackground)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(title, fontSize = 22.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
        Text(subtitle, fontSize = 12.sp, color = TextSecondary)
        Spacer(modifier = Modifier.height(20.dp))
        GlassCard(modifier = Modifier.fillMaxWidth(), content = content)
    }
}
