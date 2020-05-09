package cn.thens.dex_gradle

import com.android.tools.r8.D8
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import java.io.File

open class D8Task : DefaultTask() {
    var enableDesugaring: Boolean = true
    val libs = mutableListOf<File>()
    val classPaths = mutableListOf<File>()

    @TaskAction
    fun dex() {
        val output = outputs.files.first()
        val args = mutableListOf<String>()
        args.add("--release")
        args.add("--output")
        args.add(output.absolutePath)
        if (enableDesugaring) {
            libs.forEach { args.add("--lib"); args.add(it.absolutePath) }
            classPaths.forEach { args.add("--classpath"); args.add(it.absolutePath) }
        } else {
            args.add("--no-desugaring")
        }
        inputs.files.forEach { args.add(it.absolutePath) }
        MyLogger.log("d8 args: $args")
        D8.main(args.toTypedArray())
    }
}