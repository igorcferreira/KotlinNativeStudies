import org.gradle.api.DefaultTask
import org.gradle.api.file.FileSystemLocation
import org.gradle.api.file.RegularFile
import org.gradle.api.provider.Provider
import org.gradle.api.provider.ProviderFactory
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import java.io.File
import javax.inject.Inject

abstract class PackageFramework @Inject constructor(
    private val providerFactory: ProviderFactory
): DefaultTask() {
    @Input
    lateinit var outputDir: FileSystemLocation
    @Input
    lateinit var framework: Provider<RegularFile>
    @Input
    lateinit var packageTemplate: FileSystemLocation

    @TaskAction
    fun doTaskAction() {
        val frameworkFile = framework.get().asFile
        if (!frameworkFile.exists()) {
            throw RuntimeException("XCFramework does not exist: ${frameworkFile.absolutePath}")
        }

        val outputFileDir = File(outputDir.asFile, frameworkFile.nameWithoutExtension)
        val zipFile = zip(frameworkFile, outputFileDir)
        val checksum = calculateCheckSum(zipFile)
        packageFramework(zipFile, packageTemplate.asFile, outputFileDir)
        print("Framework generated at $outputDir. Zip checksum is $checksum")
    }

    private fun packageFramework(
        file: File,
        template: File,
        outputDir: File
    ) {
        val packageFile = File(outputDir, "Package.swift")
        val templateContent = template.readText()
            .replace("\${FRAMEWORK_NAME}", file.nameWithoutExtension)
            .replace("\${FRAMEWORK_PATH}", file.name)
        if (packageFile.exists()) {
            packageFile.delete()
        }
        packageFile.writeText(templateContent)
    }

    private fun calculateCheckSum(
        file: File
    ): String {
        val output = providerFactory.exec {
            workingDir(file.parentFile)
            isIgnoreExitValue = true
            executable("swift")
            args(listOf(
                "package",
                "compute-checksum",
                file.absolutePath
            ))
        }

        if (!output.standardError.asText.orNull.isNullOrBlank()) {
            throw RuntimeException("Unable to zip framework: ${output.standardError.asText.orNull}")
        }

        return output.standardOutput.asText
            .orNull?.trim()
            ?: throw RuntimeException("Unable to calculate checksum for: ${file.absolutePath}")
    }

    private fun zip(
        frameworkFile: File,
        outputDir: File
    ): File {
        val zipOperation = providerFactory.exec {
            workingDir(frameworkFile.parentFile)
            isIgnoreExitValue = true
            executable("zip")
            args(listOf(
                "-r",
                frameworkFile.nameWithoutExtension + ".zip",
                frameworkFile.name
            ))
        }

        if (!zipOperation.standardError.asText.orNull.isNullOrBlank()) {
            throw RuntimeException("Unable to zip framework: ${zipOperation.standardError.asText.orNull}")
        }

        val tempFramework = File(frameworkFile.parentFile, frameworkFile.nameWithoutExtension + ".zip")
        val zipFile = File(outputDir, frameworkFile.nameWithoutExtension + ".zip")
        tempFramework.copyRecursively(target = zipFile, overwrite = true)
        tempFramework.delete()
        return zipFile
    }
}
