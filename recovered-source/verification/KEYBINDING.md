# LHS Lobby keybinding verification

Verified with javap against the Lobby/Auth class files extracted
from the user's current LHSMENU build:

- Key translation key: `key.lhs_lobby.open`
- GLFW key code: `77` (M)
- Registered by: `net.lhs.lobby.LHSLobbyClient.registerKeys`
- Input handler: `net.lhs.lobby.LHSLobbyClient$ClientEvents.keyInput`
- GUI opened: `net.lhs.lobby.LobbyScreen`

The adjacent bytecode reports are generated from the uploaded
compiled class files. This check does not depend on CFR's output.
