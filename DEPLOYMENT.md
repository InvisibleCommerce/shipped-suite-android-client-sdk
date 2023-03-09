# How to deploy Shipped Suite Android SDK

This document will take you through the process of creating/updating Shipped Suite Android SDK.

## Getting started

Open `gradle.properties` and update the `VERSION_NAME`.

### Publish to MavenCentral
1. Open [Actions](https://github.com/InvisibleCommerce/shipped-suite-android-client-sdk/actions/workflows/publish.yml) in the Github.
2. Choose `Publish` workflow and click `Run workflow`.
3. Choose the `main` branch and enter the version name (e.g., 1.0.0).
4. Run it.

### Publish to JitPack
1. Open [JitPack](https://www.jitpack.io/).
2. Enter [Repo URL](https://github.com/InvisibleCommerce/shipped-suite-android-client-sdk) in the field and look up.
3. Click `Get it`.