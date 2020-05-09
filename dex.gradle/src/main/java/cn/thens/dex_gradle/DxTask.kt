package cn.thens.dex_gradle

import com.android.tools.r8.compatdx.CompatDx
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

open class DxTask : DefaultTask() {
    @TaskAction
    fun dex() {
        val output = outputs.files.first()
        val args = mutableListOf<String>()
        args.add("--dex")
        args.add("--output")
        args.add(output.absolutePath)
        inputs.files.forEach { args.add(it.absolutePath) }
        MyLogger.log("dx args: $args")
        CompatDx.main(args.toTypedArray())
    }
}