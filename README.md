# Android Template Compose <br> <a href="https://github.com/deepfine/mob_android_template_compose/actions"><img alt="Build Status" src="https://github.com/deepfine/mob_android_template_compose/actions/workflows/build.yml/badge.svg"/></a><br>

## 🚀 Overview
Scalable and maintainable Android application template built with: 
- **Kotlin** 
- **Jetpack Compose**
- **MVI (Model-View-Intent) architecture**
- **Multi-module structure**

<br>


## 🚀 Quick Start
### Create a new repository
<img width="981" height="245" alt="create_a_new_repository" src="https://github.com/user-attachments/assets/5081fd52-0d31-468d-ac09-321b18456606" />

### Add repository to Runner groups Repository access
<img width="708" height="133" alt="add_repository_access" src="https://github.com/user-attachments/assets/5cad5211-7312-46a8-a84e-939ad6e0123d" />

<br>
<br>

## 🗂 Project structure
```
/
├── app/                 # Appplication module 
├── data-api/            # Data layer interfaces
├── data-impl/           # Data layer implementations
├── navigator/           # Navigation abstraction
├── presentation/        # Components shared in presentation layers
│     └── sub-modules/   # UI, ViewModels, MVI contracts
├── build-logic/         # Custom Gradle plugins & convention logic
├── buildconfig-stub/    # BuildConfig template module
├── gradle/              # Gradle wrapper & version catalogs
└── scripts/             # Utility scripts
```

<br>

## ⚙️ Requirements
- **Android Studio Narwhal or newer**
- **JDK 17**

<br>

## 📐 Conventions
### Repository Name
`mob_${Project Name}_android_${device}`</br>
✅ mob_dao_android / mob_dao_android_glass


