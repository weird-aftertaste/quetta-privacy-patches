package app.quetta.patches

import app.morphe.patcher.patch.PatchException
import app.morphe.patcher.patch.rawResourcePatch

/**
 * Chromium keeps its own copies of some endpoints inside libchrome.so (reporting tables,
 * crash uploader, vendor URLs). Bytecode patches cannot reach those, so rewrite the libraries.
 */
@Suppress("unused")
val quettaNativeEndpointsPatch = rawResourcePatch(
    name = "Strip telemetry endpoints from native libraries",
    description = "Rewrites tracking hosts embedded in lib*.so, which bytecode patches cannot reach.",
    default = false
) {
    execute {
        val libraries = listApkEntries("lib/").filter { it.endsWith(".so") }
        var filesChanged = 0
        for (entry in libraries) {
            val file = get(entry)  // copy=true extracts the library to the working dir
            val original = file.readBytes()
            var content = String(original, Charsets.ISO_8859_1)
            var changed = false
            for ((from, to) in DEAD_HOSTS) {
                if (content.contains(from)) {
                    content = content.replace(from, to)
                    changed = true
                }
            }
            if (!changed) continue
            file.writeBytes(content.toByteArray(Charsets.ISO_8859_1))
            filesChanged++
        }
        if (filesChanged == 0) throw PatchException("no tracking hosts found in native libraries")
    }
}
