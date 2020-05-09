plugins {
    `kotlin-dsl`
    `maven-publish`
    id("com.github.dcendents.android-maven")
}

group = "com.github.7hens"
version = "-SNAPSHOT"

repositories {
    google()
    jcenter()
    mavenLocal()
}

gradlePlugin {
    plugins {
        register("dex.gradle") {
            id = "dex.gradle"
            implementationClass = "cn.thens.dex_gradle.DexGradlePlugin"
        }
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    compileOnly(gradleApi())
    implementation(localGroovy())
    implementation("com.android.tools.build:gradle:3.5.1")
}
