buildscript {
    val agp_version by extra("8.2.0")
}
// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.2.0" apply false
    id("org.jetbrains.kotlin.android") version "1.8.0" apply false
    id ("io.realm.kotlin") version "1.11.0" apply false
}

// I had to update to JVM 19 since my library is on 19. lol
allprojects {
    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
        kotlinOptions {
            jvmTarget = "19"
        }
    }
}