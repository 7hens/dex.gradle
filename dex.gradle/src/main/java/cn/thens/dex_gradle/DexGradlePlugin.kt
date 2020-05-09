package cn.thens.dex_gradle

import com.android.build.gradle.AppExtension
import com.android.build.gradle.BaseExtension
import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.api.ApplicationVariant
import com.android.build.gradle.api.BaseVariant
import com.android.build.gradle.api.LibraryVariant
import org.gradle.api.Plugin
import org.gradle.api.Project
import java.io.File

class DexGradlePlugin : Plugin<Project> {
    override fun apply(project: Project) {
        try {
            val androidApp = project.extensions.findByType(AppExtension::class.java)
            val androidLib = project.extensions.findByType(LibraryExtension::class.java)
            val android = androidApp ?: androidLib ?: throw RuntimeException("not a android module")
            project.afterEvaluate {
                (androidApp?.applicationVariants ?: androidLib?.libraryVariants)?.forEach {
                    createDexTask(project, android, it)
                }
            }
        } catch (e: Throwable) {
            MyLogger.error(e)
        }
    }

    private fun createDexTask(project: Project, android: BaseExtension, variant: BaseVariant) {
        val yaVariantName = variant.name.capitalize()
        val enableD8 = project.booleanProperty("android.enableD8", true)
        val enableD8Desugaring = project.booleanProperty("android.enableD8.desugaring", true)
        val dexTaskClass = if (enableD8) D8Task::class else DxTask::class
        project.tasks.register("makeDex$yaVariantName", dexTaskClass.java) {
            group = "build"
            dependsOn("assemble$yaVariantName")
            outputs.files(File(project.buildDir, "outputs/jar/${project.name}-${variant.name}.jar"))
            val packageTaskProvider = (variant as? ApplicationVariant)?.packageApplicationProvider
                ?: (variant as? LibraryVariant)?.packageLibraryProvider
                ?: throw RuntimeException("unsupported variant $variant")
            packageTaskProvider.get().inputs.files
                .filter { it.extension == "jar" }
                .forEach { inputs.file(it) }
            if (this is D8Task && enableD8Desugaring) {
                enableDesugaring = enableD8Desugaring
                libs.addAll(android.bootClasspath)
                classPaths.addAll(variant.javaCompileProvider.get().classpath.files)
                classPaths.addAll(variant.javaCompileProvider.get().outputs.files)
            }
        }
    }

    private fun Project.booleanProperty(property: String, defaultValue: Boolean): Boolean {
        val value = properties[property] ?: return defaultValue
        return value.toString().toBoolean()
    }
}

