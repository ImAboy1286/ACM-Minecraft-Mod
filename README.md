# ACM Open Source Minecraft Mod

ACM is a Fabric 1.21.1 mod framework. Its first gameplay change extends
vanilla beacons with Saturation as an additional primary effect at beacon
tier 3 or higher and as a level II secondary effect at tier 4. The project
also contains the build configuration and entrypoints needed to add more
Minecraft content.

## Requirements

- Java 21
- Gradle 9.7.1 or a compatible Gradle installation
- Minecraft 1.21.1
- Fabric Loader and Fabric API, downloaded by Gradle during the build

The repository currently does not include a Gradle wrapper. If one is added,
prefer `gradlew.bat` on Windows and `./gradlew` on macOS/Linux.

## Build the mod

From the repository root, run:

```powershell
gradle build
```

The remapped JAR is created in `build/libs/`. The exact filename follows
`mod_version` and `archives_base_name` in `gradle.properties`.

## Install the mod in Minecraft

1. Install Fabric Loader for Minecraft 1.21.1.
2. Install the matching Fabric API JAR in the Minecraft `mods` directory.
3. Copy the ACM JAR from `build/libs/` into that directory.
4. Start the Fabric 1.21.1 profile.

On Windows, the default mods directory is:

```text
%APPDATA%\.minecraft\mods
```

## Live development updates

Run the isolated Fabric development client with:

```powershell
gradle runClient
```

Resource-only changes can usually be reloaded with `F3+T`. Java code,
registries, and entrypoint changes require stopping and rerunning
`gradle runClient`. Minecraft does not reliably replace loaded mod classes
while a game is running.

## Repository layout

```text
.
├── build.gradle                    Gradle and Fabric Loom configuration
├── gradle.properties               Minecraft, Fabric, and mod versions
├── settings.gradle                 Gradle project and plugin repositories
├── .gitignore                      Local caches, builds, logs, and IDE files
├── LICENSE                         MIT license
├── README.md                       Project and development documentation
└── src/main
    ├── java/com/acm
    │   ├── AcmMod.java             Server/common Fabric entrypoint
    │   ├── client/AcmClient.java   Client-only Fabric entrypoint
    │   └── mixin/
    │       └── BeaconBlockEntityMixin.java
    │                                  Adds Saturation to tier-3 beacon choices
    └── resources
        ├── acm.mixins.json          Mixin configuration
        └── fabric.mod.json          Fabric metadata and entrypoints
```

## Adding features

- Register blocks and block items in a new block registration class.
- Register entities, attributes, and behavior in an entity package.
- Register gameplay hooks in a dedicated event package.
- Register client renderers in `AcmClient`.
- Add models, translations, and other assets under
  `src/main/resources/assets/acm`.

Keep server-safe code in the common entrypoint and load client-only classes
from `AcmClient`. Update this README's gameplay overview whenever new
features are introduced.

## Current gameplay changes

### Vanilla Changes

- Tier-3 and tier-4 beacons can select Saturation as their primary effect.
- A tier-4 beacon can select Saturation as its secondary effect, and it applies
  Saturation at level II.
- Saturation selections are accepted by the same validation used for saved
  vanilla beacon effects.
- Saturation uses vanilla beacon range, application interval, and duration.
- The change is applied through a targeted `BeaconBlockEntity` mixin without
  changing beacon geometry or timing.

### New Blocks

None.

### New Entities

None.
