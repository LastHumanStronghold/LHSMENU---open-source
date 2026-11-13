# LHSMENU recovered source

Reconstructed from the distributed LHSMENU.jar using CFR 0.152.
Includes Java sources for all three embedded Forge mods and all
non-class resources from the original JAR.

## Modules
- mainlhs — 1.5.0
- lhs_auth — 1.1.0
- lhs_lobby — 1.1.0

## Important restored packages
- `com/mainlhs/` — main LHS menu and shared systems
- `ru/lhs/auth/client/` — in-game authentication interface
- `net/lhs/lobby/` — lobby GUI, Tab list and M keybinding

The Lobby/Auth packages are decompiled class-by-class because CFR's
whole-JAR pass omitted them. CI checks that their expected source files
exist. The `verification/` folder also records the M key binding
directly from the original compiled bytecode.

These are decompiled sources, not the original developer-authored
project. Comments and original local variable names cannot be recovered
from bytecode. Some Forge/Minecraft references may retain production
obfuscated names, so a clean Gradle rebuild is not guaranteed without
matching mappings and dependencies.

The repository-root LHSMENU.jar is an older main-module binary. The additional
Lobby/Auth class files and lobby translations are supplied under
recovery-input/ and SHA-256 verified during each recovery run.
