package app.quetta.patches

import app.morphe.patcher.extensions.InstructionExtensions.getInstruction
import app.morphe.patcher.extensions.InstructionExtensions.replaceInstruction
import app.morphe.patcher.patch.BytecodePatchContext
import com.android.tools.smali.dexlib2.iface.ClassDef
import com.android.tools.smali.dexlib2.iface.instruction.OneRegisterInstruction
import com.android.tools.smali.dexlib2.iface.instruction.ReferenceInstruction
import com.android.tools.smali.dexlib2.iface.reference.StringReference

private fun String.replaceHosts(hosts: List<Pair<String, String>>): String {
    var value = this
    for ((from, to) in hosts) {
        if (value.contains(from)) value = value.replace(from, to)
    }
    return value
}

/**
 * Rewrite every string constant that contains one of [hosts].
 *
 * Returns the number of rewritten instructions. Longest hosts are matched first (see [Hosts]),
 * so a replacement never feeds a later one.
 */
internal fun BytecodePatchContext.rewriteHosts(hosts: List<Pair<String, String>>): Int {
    val classesWithHosts = mutableListOf<ClassDef>()
    classDefForEach { classDef ->
        val found = classDef.methods.any { method ->
            method.implementation?.instructions?.any { instruction ->
                val reference = (instruction as? ReferenceInstruction)?.reference
                reference is StringReference && reference.string.replaceHosts(hosts) != reference.string
            } == true
        }
        if (found) classesWithHosts += classDef
    }

    var rewritten = 0
    for (classDef in classesWithHosts) {
        for (method in mutableClassDefBy(classDef).methods) {
            val instructions = method.implementation?.instructions ?: continue
            for (index in instructions.indices) {
                val instruction = method.getInstruction(index)
                val reference = (instruction as? ReferenceInstruction)?.reference ?: continue
                if (reference !is StringReference) continue
                val replaced = reference.string.replaceHosts(hosts)
                if (replaced == reference.string) continue
                val register = (instruction as OneRegisterInstruction).registerA
                method.replaceInstruction(index, "const-string v$register, \"$replaced\"")
                rewritten++
            }
        }
    }
    return rewritten
}
