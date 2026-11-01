package app.quetta.patches

/**
 * Hosts that report usage, crashes or browsing metadata.
 *
 * Replacements are generated from the source host so both sides always have the same byte
 * length; a length mismatch shifts everything after it in a native library and breaks dlopen.
 * `.invalid` is reserved by RFC 2606, so it can never resolve.
 */
private fun deadHost(host: String): String {
    require(host.length >= 10) { "host too short to neutralise: $host" }
    return "b" + "0".repeat(host.length - 9) + ".invalid"
}

private val TRACKED_HOSTS = listOf(
    // GA4 / Firebase Analytics / Crashlytics / FCM identifiers
    "firebaseperusertopics-pa.googleapis.com",
    "firebaseinstallations.googleapis.com",
    "firebase-settings.crashlytics.com",
    "chromereporting-pa.googleapis.com",
    "fcmregistrations.googleapis.com",
    "pagead2.googlesyndication.com",
    "google-analytics.com",
    "app-measurement.com",
    // crash dumps
    "crashpad.chromium.org",
    "sentry.thefrodo.com",
    // vendor infrastructure: update poll, favicon proxy, start page, video AI, share links
    "test-update.quetta.net",
    "test-omaha.quetta.net",
    "update.quetta.net",
    "omaha.quetta.net",
    "qt.quetta.net",
    "ai.quetta.net",
    "go.quetta.net",
    "f.quetta.net",
    "bcp.quetta.net",
)

/** Longest first, so a shorter host never rewrites part of a longer one. */
internal val DEAD_HOSTS: List<Pair<String, String>> =
    TRACKED_HOSTS.sortedByDescending { it.length }.map { it to deadHost(it) }

internal fun String.deadHosts(): String {
    var value = this
    for ((from, to) in DEAD_HOSTS) {
        if (value.contains(from)) value = value.replace(from, to)
    }
    return value
}
