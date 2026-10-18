package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.ui.theme.MyApplicationTheme

// Data Class Anggota (Diubah ke Bahasa Indonesia)
data class Member(
    val id: Int,
    val nama: String,
    val nim: String,
    val jabatan: String,
    val bio: String,
    val avatarColor: Color,
    val skill: List<String>,
    val hobi: List<String>
)

// Data Class Preset Tema
data class ThemePreset(
    val name: String,
    val bgGradient: List<Color>,
    val cardBg: Color,
    val textColor: Color,
    val accentColor: Color
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainNavigationApp(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun MainNavigationApp(modifier: Modifier = Modifier) {
    var selectedMember by remember { mutableStateOf<Member?>(null) }

    val themes = remember {
        listOf(
            ThemePreset(
                "Light Clean",
                listOf(Color(0xFFF1F5F9), Color(0xFFE2E8F0)),
                Color.White,
                Color(0xFF0F172A),
                Color(0xFF2563EB)
            ),
            ThemePreset(
                "Midnight Gold",
                listOf(Color(0xFF0F172A), Color(0xFF1E1B4B), Color(0xFF31103F)),
                Color(0xFF1E293B).copy(alpha = 0.95f),
                Color.White,
                Color(0xFFF59E0B)
            ),
            ThemePreset(
                "Cyber Neon",
                listOf(Color(0xFF0D0221), Color(0xFF020B14), Color(0xFF15002A)),
                Color(0xFF111827).copy(alpha = 0.95f),
                Color.White,
                Color(0xFF06B6D4)
            ),
            ThemePreset(
                "Emerald Glass",
                listOf(Color(0xFF064E3B), Color(0xFF022C22), Color(0xFF0F172A)),
                Color(0xFF065F46).copy(alpha = 0.85f),
                Color.White,
                Color(0xFF34D399)
            )
        )
    }

    var currentTheme by remember { mutableStateOf(themes[0]) }

    val members = remember {
        listOf(
            Member(
                1, "Muhammad Naufal", "2024240028", "Mahasiswa",
                "Mahasiswa Aktif Sistem Informasi Tahun 2024",
                Color(0xFF2563EB),
                listOf("yang penting ngomong"),
                listOf("suka penasaran")
            ),
            Member(
                2, "Kayla Ramadhani", "2024240010", "Mahasiswi",
                "Mahasiswa Aktif Sistem Informasi Yang suka Roblox.",
                Color(0xFFDB2777),
                listOf("Parkour Roblox, joki Game Horor"),
                listOf("gak ada")
            ),
            Member(
                3, "Kevin Fajar Pratama", "2024240003", "Mahasiswa",
                "Mahasiswa Akif sistem informasi tahun 2024",
                Color(0xFF059669),
                listOf("bisa ngoding, gitu gitu"),
                listOf("game")
            ),
            Member(
                4, "Radika Widi Putra", "2024240029", "Mahasiswa",
                "Mahasiswa Aktif Sistem Informasi Tahun 2024",
                Color(0xFFD97706),
                listOf("Mengerjai Orang lain"),
                listOf("Belajar Dengan Tekun, tapi besok")
            ),
            Member(
                5, "Michael Joshua Sibarani", "2024240034", "Mahasiswa",
                "Mahasiswa Kurang Aktif Sistem Informasi Tahun 2024",
                Color(0xFF7C3AED),
                listOf("masih di cari"),
                listOf("Menghilang pada saat kelas dan kerja kelompok")
            ),
            Member(
                6, "M. Ghatan Naufal Hakin", "2024240032", "Mahasiswa",
                "Mahasiswa Aktif Sistem Informasi Tahun 2024",
                Color(0xFF0891B2),
                listOf("Berisik di kelas"),
                listOf("Ngonser, admin kamboja")
            )
        )
    }

    Crossfade(targetState = selectedMember, label = "PageTransition") { member ->
        if (member == null) {
            GroupListScreen(
                modifier = modifier,
                members = members,
                themes = themes,
                currentTheme = currentTheme,
                onThemeChange = { currentTheme = it },
                onMemberClick = { selectedMember = it }
            )
        } else {
            BackHandler {
                selectedMember = null
            }
            MemberDetailScreen(
                modifier = modifier,
                member = member,
                currentTheme = currentTheme,
                onBackClick = { selectedMember = null }
            )
        }
    }
}

// HALAMAN 1: DAFTAR ANGGOTA
@Composable
fun GroupListScreen(
    modifier: Modifier,
    members: List<Member>,
    themes: List<ThemePreset>,
    currentTheme: ThemePreset,
    onThemeChange: (ThemePreset) -> Unit,
    onMemberClick: (Member) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }

    val filteredMembers = members.filter {
        it.nama.contains(searchQuery, ignoreCase = true) || it.jabatan.contains(searchQuery, ignoreCase = true)
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(currentTheme.bgGradient))
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            contentPadding = PaddingValues(top = 24.dp, bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                    Surface(
                        color = currentTheme.accentColor.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(50.dp)
                    ) {
                        Text(
                            text = "PROJECT ANDROID STUDIO",
                            color = currentTheme.accentColor,
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Kelompok 2",
                        fontSize = 30.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = currentTheme.textColor
                    )
                    Text(
                        text = "Praktikum Pemrograman Android & Aplikasi Mobilee",
                        style = MaterialTheme.typography.bodyMedium,
                        color = currentTheme.textColor.copy(alpha = 0.7f),
                        textAlign = TextAlign.Center
                    )
                }
            }

            item {
                Column {
                    Text(
                        text = "Pilih Tema Tampilan:",
                        style = MaterialTheme.typography.labelSmall,
                        color = currentTheme.textColor.copy(alpha = 0.6f),
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        items(themes) { theme ->
                            val isSelected = theme == currentTheme
                            Surface(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .clickable { onThemeChange(theme) }
                                    .border(
                                        width = if (isSelected) 2.dp else 1.dp,
                                        color = if (isSelected) theme.accentColor else Color.Gray.copy(alpha = 0.2f),
                                        shape = RoundedCornerShape(12.dp)
                                    ),
                                color = theme.cardBg
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(12.dp)
                                            .clip(CircleShape)
                                            .background(theme.accentColor)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = theme.name,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = theme.textColor,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                    )
                                }
                            }
                        }
                    }
                }
            }

            item {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Cari nama atau jabatan...", color = currentTheme.textColor.copy(alpha = 0.5f)) },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = currentTheme.accentColor,
                        unfocusedBorderColor = Color.Gray.copy(alpha = 0.2f),
                        focusedContainerColor = currentTheme.cardBg,
                        unfocusedContainerColor = currentTheme.cardBg,
                        focusedTextColor = currentTheme.textColor,
                        unfocusedTextColor = currentTheme.textColor
                    ),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                Text(
                    text = "Daftar Anggota Tim (${filteredMembers.size})",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = currentTheme.textColor
                )
            }

            items(filteredMembers, key = { it.id }) { member ->
                ElevatedCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(18.dp))
                        .clickable { onMemberClick(member) },
                    shape = RoundedCornerShape(18.dp),
                    elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp),
                    colors = CardDefaults.elevatedCardColors(containerColor = currentTheme.cardBg)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = member.avatarColor.copy(alpha = 0.12f),
                            border = androidx.compose.foundation.BorderStroke(2.dp, member.avatarColor),
                            modifier = Modifier.size(60.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = member.nama.take(1),
                                    fontWeight = FontWeight.Black,
                                    fontSize = 24.sp,
                                    color = member.avatarColor
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = member.nama,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = currentTheme.textColor
                            )
                            Text(
                                text = "NIM: ${member.nim}",
                                style = MaterialTheme.typography.bodySmall,
                                color = currentTheme.textColor.copy(alpha = 0.6f)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = member.jabatan,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = member.avatarColor
                            )
                        }

                        Surface(
                            color = member.avatarColor.copy(alpha = 0.12f),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text(
                                text = "Detail ➔",
                                style = MaterialTheme.typography.labelSmall,
                                color = member.avatarColor,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

// HALAMAN 2: DETAIL BIODATA
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MemberDetailScreen(
    modifier: Modifier,
    member: Member,
    currentTheme: ThemePreset,
    onBackClick: () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(currentTheme.bgGradient))
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            contentPadding = PaddingValues(top = 20.dp, bottom = 36.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Tombol Kembali
            item {
                ElevatedButton(
                    onClick = onBackClick,
                    colors = ButtonDefaults.elevatedButtonColors(
                        containerColor = currentTheme.cardBg,
                        contentColor = currentTheme.textColor
                    ),
                    shape = RoundedCornerShape(12.dp),
                    elevation = ButtonDefaults.elevatedButtonElevation(defaultElevation = 2.dp)
                ) {
                    Text("← Kembali ke Daftar", fontWeight = FontWeight.Bold)
                }
            }

            // Hero Header Card
            item {
                ElevatedCard(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    elevation = CardDefaults.elevatedCardElevation(defaultElevation = 3.dp),
                    colors = CardDefaults.elevatedCardColors(containerColor = currentTheme.cardBg)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = member.avatarColor.copy(alpha = 0.12f),
                            border = androidx.compose.foundation.BorderStroke(3.dp, member.avatarColor),
                            modifier = Modifier.size(96.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = member.nama.take(1),
                                    fontSize = 44.sp,
                                    fontWeight = FontWeight.Black,
                                    color = member.avatarColor
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = member.nama,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = currentTheme.textColor,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Surface(
                            color = member.avatarColor.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(50.dp)
                        ) {
                            Text(
                                text = member.jabatan,
                                style = MaterialTheme.typography.labelMedium,
                                color = member.avatarColor,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                            )
                        }
                    }
                }
            }

            // Kartu Informasi Biodata
            item {
                ElevatedCard(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp),
                    colors = CardDefaults.elevatedCardColors(containerColor = currentTheme.cardBg)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Header Judul
                        Text(
                            text = "📌 BIODATA ANGGOTA",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.ExtraBold,
                            color = member.avatarColor
                        )

                        HorizontalDivider(color = Color.Gray.copy(alpha = 0.15f))

                        // Row NIM
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "NIM",
                                style = MaterialTheme.typography.bodyMedium,
                                color = currentTheme.textColor.copy(alpha = 0.6f)
                            )
                            Surface(
                                color = Color.Gray.copy(alpha = 0.08f),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = member.nim,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = currentTheme.textColor,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                )
                            }
                        }

                        HorizontalDivider(color = Color.Gray.copy(alpha = 0.1f))

                        // Seksi Tentang Saya
                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text(
                                text = "Tentang Saya",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = currentTheme.textColor
                            )
                            Text(
                                text = member.bio,
                                style = MaterialTheme.typography.bodyMedium,
                                color = currentTheme.textColor.copy(alpha = 0.75f),
                                lineHeight = 22.sp
                            )
                        }

                        HorizontalDivider(color = Color.Gray.copy(alpha = 0.1f))

                        // Seksi Skill
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(
                                text = "Skill & Keahlian",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = currentTheme.textColor
                            )

                            FlowRow(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                member.skill.forEach { item ->
                                    Surface(
                                        color = member.avatarColor.copy(alpha = 0.12f),
                                        shape = RoundedCornerShape(10.dp),
                                        border = androidx.compose.foundation.BorderStroke(1.dp, member.avatarColor.copy(alpha = 0.3f))
                                    ) {
                                        Text(
                                            text = item,
                                            style = MaterialTheme.typography.labelSmall,
                                            fontWeight = FontWeight.SemiBold,
                                            color = member.avatarColor,
                                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                                        )
                                    }
                                }
                            }
                        }

                        HorizontalDivider(color = Color.Gray.copy(alpha = 0.1f))

                        // Seksi Hobi
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(
                                text = "Hobi & Minat",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = currentTheme.textColor
                            )

                            FlowRow(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                member.hobi.forEach { item ->
                                    Surface(
                                        color = Color.Gray.copy(alpha = 0.08f),
                                        shape = RoundedCornerShape(10.dp)
                                    ) {
                                        Text(
                                            text = "• $item",
                                            style = MaterialTheme.typography.labelSmall,
                                            fontWeight = FontWeight.Medium,
                                            color = currentTheme.textColor.copy(alpha = 0.8f),
                                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun GreetingPreview() {
    MyApplicationTheme {
        MainNavigationApp()
    }
}