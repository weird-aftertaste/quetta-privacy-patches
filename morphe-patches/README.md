# Quetta Privacy Patches

Morphe patches that remove analytics, crash reporting and vendor tracking endpoints from
Quetta Browser (`net.quetta.browser`), harden its network security config, and drop
`QUERY_ALL_PACKAGES`.

Apply with [Morphe Manager](https://github.com/MorpheApp/morphe-manager) or any Morphe patcher.
The input must be a single APK; merge Quetta's split bundle first (APKEditor `m`).

Patches declare compatibility with Quetta Browser 2.2.0 and refuse to run on other packages.

## 🩹 Patches list

<!-- PATCHES_START EXPANDED -->

<!-- Do not modify this section by hand. The patch list is generated when release.yml creates a new release. -->

#### A list of patches will automatically be shown here after the first release is created.

<!-- PATCHES_END -->

## 🛠️ Building locally

- `./gradlew buildAndroid`
- The bundle is written to `patches/build/libs/patches-*.mpp`
- Apply it with [Morphe Desktop](https://github.com/MorpheApp/morphe-desktop) or the CLI in `../morphe-apply`
