# 🍽️ YourDietBuddy - Android Health & Nutrition App

YourDietBuddy is a nutrition-focused Android app built using **Jetpack Compose** and **Kotlin**, designed to help users maintain a healthy diet. It includes features like BMI calculation, daily meal tracking, and personalized food recommendations.

---

## 🚀 Features

- 🏠 **Dashboard**: Displays user's meal summary with calorie count and quick actions.
- 📅 **All Meals View**: View the full list of meals for the day, categorized by time.
- 🍽️ **Detail Screen**: View detailed information about a selected meal.
- ➕ **Add Meal (Tambah)**: (Placeholder) A button to allow meal input/creation.
- 👤 **Profile**: (Placeholder) User profile section.
- 📉 **BMI Calculator**: (Planned) Calculate Body Mass Index and suggest food types accordingly.
- 📊 **Charts & Summary**: (Planned) Visualize calorie consumption via graphs.

---

## 🛠️ Built With

- [Kotlin](https://kotlinlang.org/)
- [Jetpack Compose](https://developer.android.com/jetpack/compose)
- [Navigation Component](https://developer.android.com/jetpack/compose/navigation)
- [Material 3](https://m3.material.io/)
- [Android Studio](https://developer.android.com/studio)

---

## 📷 Screenshots


## 🔧 Project Structure


## 🍽️ Conclusion
YourDietBuddy adalah aplikasi pelacak diet bagi pengguna yang ingin mendapatkan gaya hidup sehat. 
Pengguna bisa mencari langkah apa yang bisa ditempuh dengan cara memasukkan berat badan dan tinggi 
badan lalu kalkulator BMI yang terdapat pada aplikasi akan menggolongkan pengguna,apakah pengguna 
termasuk ke dalam kategori kekurangan gizi/normal/obesitas.Pengguna dapat melacak total kalori dari 
makanan yang telah dikosumsi dan juga menetapkan target di dalam aplikasi. Aplikasi terdapat halaman 
beranda(dashboard) dan halaman profil.
Kami mengimplementasikan berbagai materi penting Compose seperti: UI Components: penggunaan komponen
-komponen dasar seperti Text, Button, Card, AlertDialog, dan Scaffold untuk membentuk struktur dan fungsionalitas aplikasi. 
Layouting & Styling: pemanfaatan Box, Column, Row, Modifier, padding, shadow, dan Brush untuk menciptakan tampilan yang rapi dan menarik. 
Lazy Layout: penggunaan LazyColumn dan LazyRow untuk menampilkan daftar item dengan performa tinggi dan tampilan responsif. 
State: pengelolaan state dengan remember dan mutableStateOf agar UI dapat merespons interaksi pengguna secara real-time.
Navigation: navigasi antar tampilan diatur dengan NavController dan state navigasi lokal seperti selectedNav. ViewModel:
pemisahan logika dan data menggunakan ProfileViewModel agar UI tetap bersih dan mudah dikelola.
