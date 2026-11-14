# LHSMENU recovered source

This archive combines:
- Main LHS Java sources decompiled from the repository-root LHSMENU.jar
- LHS Lobby and LHS Auth classes extracted from the current LHSMENU build
- Lobby Russian/English translations and the matching three-mod mods.toml

## Included modules
- mainlhs — 1.5.0
- lhs_auth — 1.1.0
- lhs_lobby — 1.1.0

## Important packages
- `com/mainlhs/` — main LHS menu and shared systems
- `net/lhs/lobby/` — Lobby GUI, Tab list and M keybinding
- `ru/lhs/auth/client/` — in-game authentication interface and API
- `net/lhs/lobby/QuickSkinMenuIntegration.java` — optional Quick Skin button integration for the main and pause menus

## M key
The recovered `LHSLobbyClient.java` registers
`key.lhs_lobby.open` on GLFW key code 77 (M). Its client input
handler opens `LobbyScreen`. The `verification/` directory contains
javap bytecode reports and a short verification note.

## Recovery limits
These are decompiled sources, not the original developer-authored
project. Comments and original local variable names cannot be recovered
from bytecode. Some Minecraft/Forge symbols retain production names,
so a clean Gradle rebuild is not guaranteed without matching mappings
and dependencies.

The repository-root LHSMENU.jar is an older main-module binary.
Lobby/Auth classes and translations are supplied by the checked
supplement in `recovery-input/`. CI verifies the supplement SHA-256,
the required Java files, and the M-key-to-LobbyScreen bytecode path.
