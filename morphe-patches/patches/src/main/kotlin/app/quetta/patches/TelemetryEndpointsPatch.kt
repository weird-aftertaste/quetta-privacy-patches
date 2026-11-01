package app.quetta.patches

import app.morphe.patcher.extensions.InstructionExtensions.getInstruction
import app.morphe.patcher.extensions.InstructionExtensions.replaceInstruction
import app.morphe.patcher.patch.PatchException
import app.morphe.patcher.patch.bytecodePatch
import com.android.tools.smali.dexlib2.iface.ClassDef
import com.android.tools.smali.dexlib2.iface.instruction.OneRegisterInstruction
import com.android.tools.smali.dexlib2.iface.instruction.ReferenceInstruction
import com.android.tools.smali.dexlib2.iface.reference.StringReference

private fun ClassDef.hasTrackedStrings(): Boolean = methods.any { method ->
    method.implementation?.instructions?.any { instruction ->
        val reference = (instruction as? ReferenceInstruction)?.reference
        reference is StringReference && reference.string.deadHosts() != reference.string
    } == true
}

@Suppress("unused")
val quettaTelemetryEndpointsPatch = bytecodePatch(
    name = "Strip telemetry endpoints",
    description = "Rewrites analytics, crash-reporting and vendor tracking hosts to a " +
        "non-resolvable domain, so nothing can be uploaded.",
    default = false
) {
    execute {
        val targets = mutableListOf<ClassDef>()
        classDefForEach { classDef ->
            if (classDef.hasTrackedStrings()) targets += classDef
        }

        var rewritten = 0
        for (classDef in targets) {
            for (method in mutableClassDefBy(classDef).methods) {
                val instructions = method.implementation?.instructions ?: continue
                for (index in instructions.indices) {
                    val instruction = method.getInstruction(index)
                    val reference = (instruction as? ReferenceInstruction)?.reference ?: continue
                    if (reference !is StringReference) continue
                    val replaced = reference.string.deadHosts()
                    if (replaced == reference.string) continue
                    val register = (instruction as OneRegisterInstruction).registerA
                    method.replaceInstruction(index, "const-string v$register, \"$replaced\"")
                    rewritten++
                }
            }
        }

        if (rewritten == 0) throw PatchException(
            "no tracking hosts found - the app layout changed, update the host list"
        )
    }
}
