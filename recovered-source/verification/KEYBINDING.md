# LHS Lobby keybinding verification

Verified against the original LHSMENU.jar with javap:

- Key translation key: `key.lhs_lobby.open`
- GLFW key code: `77` (M)
- Registered by: `net.lhs.lobby.LHSLobbyClient.registerKeys`
- Input handler: `net.lhs.lobby.LHSLobbyClient$ClientEvents.keyInput`
- GUI opened: `net.lhs.lobby.LobbyScreen`

The two adjacent bytecode reports are generated directly from the
distributed JAR. This check does not depend on CFR's output.
