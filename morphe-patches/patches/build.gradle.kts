group = "app.quetta"

patches {
    about {
        name = "Quetta Privacy Patches"
        description = "Removes analytics, crash reporting and tracking endpoints from Quetta Browser."
        source = "https://github.com/weird-aftertaste/quetta-privacy-patches"
        author = "weird-aftertaste"
        contact = "na"
        website = "na"
        license = "GPLv3"
    }
}

dependencies {
    compileOnly(libs.gson)
}
