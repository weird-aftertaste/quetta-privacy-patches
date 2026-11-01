package app.quetta.patches

import app.morphe.patcher.patch.PatchException
import app.morphe.patcher.patch.bytecodePatch

@Suppress("unused")
val analyticsEndpointsPatch = bytecodePatch(
    name = "Strip analytics endpoints",
    description = "Points usage reporting, crash reporting and telemetry beacon hosts at a " +
        "non-resolvable domain, so they cannot be uploaded.",
    default = false
) {
    compatibleWith(COMPATIBILITY_QUETTA)

    dependsOn(nativeAnalyticsEndpointsPatch)

    execute {
        val rewritten = rewriteHosts(ANALYTICS_HOSTS)
        if (rewritten == 0) {
            throw PatchException("no analytics endpoint found - the app layout changed, update the host list")
        }
    }
}

@Suppress("unused")
val vendorEndpointsPatch = bytecodePatch(
    name = "Strip vendor tracking endpoints",
    description = "Points the favicon proxy, start page, video sniffing and share link hosts at a " +
        "non-resolvable domain. Site icons and start page suggestions stop loading.",
    default = false
) {
    compatibleWith(COMPATIBILITY_QUETTA)

    dependsOn(nativeVendorEndpointsPatch)

    execute {
        val rewritten = rewriteHosts(VENDOR_HOSTS)
        if (rewritten == 0) {
            throw PatchException("no vendor endpoint found - the app layout changed, update the host list")
        }
    }
}

@Suppress("unused")
val updatePushEndpointsPatch = bytecodePatch(
    name = "Disable update and push endpoints",
    description = "Points the component update and push registration hosts at a non-resolvable " +
        "domain. Component and ad-block list updates stop, and push notifications do not arrive.",
    default = false
) {
    compatibleWith(COMPATIBILITY_QUETTA)

    dependsOn(nativeUpdatePushEndpointsPatch)

    execute {
        val rewritten = rewriteHosts(UPDATE_PUSH_HOSTS)
        if (rewritten == 0) {
            throw PatchException("no update or push endpoint found - the app layout changed, update the host list")
        }
    }
}
