package app.quetta.patches

import app.morphe.patcher.patch.AppTarget
import app.morphe.patcher.patch.Compatibility

/** Quetta Browser 2.2.0 (versionCode 1540055), the only build these patches were validated against. */
internal val COMPATIBILITY_QUETTA = Compatibility(
    name = "Quetta Browser",
    packageName = "net.quetta.browser",
    targets = listOf(AppTarget(version = "2.2.0"))
)
