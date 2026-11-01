# Quetta Privacy Patches

Morphe patches that remove analytics, crash reporting and vendor tracking endpoints from
Quetta Browser (`net.quetta.browser`), harden its network security config, and drop
`QUERY_ALL_PACKAGES`.

Apply with [Morphe Manager](https://github.com/MorpheApp/morphe-manager) or any Morphe patcher.
The input must be a single APK; merge Quetta's split bundle first (APKEditor `m`).

Patches are written for Quetta Browser 2.2.0 and refuse to run on other packages.
