# TPA DonutSMP

A highly-configurable 1:1 replica of the DonutSMP TPA system for Paper servers (Java 17). Features Safe-Teleport logic, a modern GUI request popup, MiniMessage messages, PlaceholderAPI integration and optional bStats metrics.

## Features

- Full TPA command suite: `/tpa`, `/tpahere`, `/tpaccept`, `/tpdeny`, `/tpacancel`, `/tpatoggle`, `/tpareload`.
- Safe-Teleport (safeplace) checks destinations for lava, fire, suffocation and void and searches for the nearest safe location before teleporting.
- Warmup and cooldown support with bypass permissions for VIPs/staff.
- Clickable chat components (`[ACCEPT]` / `[DENY]`) and optional 3-row GUI request popup (player head + green/red wool).
- Configurable cancel-on-move and cancel-on-damage behavior.
- Full MiniMessage support for colored/gradient/hex messages and PlaceholderAPI support for placeholders.
- bStats metrics support (configure your plugin id in the main class).

## Commands & Permissions

- `tpa.use` — Use basic TPA commands (default: true)
- `tpa.bypass.warmup` — Bypass teleport warmup (default: op)
- `tpa.bypass.cooldown` — Bypass cooldown between teleports (default: op)
- `tpa.admin` — Reload config and admin actions (default: op)

Example commands:

- `/tpa <player>` — Request to teleport to a player.
- `/tpahere <player>` — Request that a player teleport to you.
- `/tpaccept` — Accept incoming request.
- `/tpdeny` — Deny incoming request.
- `/tpacancel` — Cancel your outgoing request.
- `/tpatoggle` — Toggle incoming requests on/off.
- `/tpareload` — Reload `config.yml` (requires `tpa.admin`).

## Config (high level)

The plugin ships with `config.yml` exposing:

- `settings.warmup` — Warmup time in seconds before teleport.
- `settings.cooldown` — Seconds between requests.
- `settings.request-expire` — Request lifetime (default 60s).
- `settings.cancel-on-move` / `settings.cancel-on-damage` — Cancel warmup.
- `settings.safe-teleport-enabled` — Toggle safeplace logic.
- `settings.safe-search-radius` / `settings.safe-search-vertical` — Safe search area.
- `gui.enabled` — Enable the 3-row GUI popup.
- `messages.*` — All user-facing text (MiniMessage-compatible).

Use MiniMessage tags and hex colors in `messages.*` entries. PlaceholderAPI placeholders are expanded when PlaceholderAPI is present.

## Installation

1. Build the plugin (Maven):

```powershell
mvn clean package
```

2. Copy the produced jar into your Paper server `plugins/` folder.
3. Start the server and configure `config.yml` as desired.

## Notes for Server Owners

- bStats ID is left as a placeholder — edit `TPADonutSMPPlugin` to set your Modrinth/bStats id.
- Toggle state for incoming requests is in-memory by default (resets on restart). If you want persistence I can add a small storage layer.
- Safeplace logic uses a configurable radius/vertical search; on very large servers you may want to reduce the search area.

## Technical

- Java: 17+
- Target: Paper (1.21+ recommended)
- Optional: PlaceholderAPI (softdepend)

## License

Include your license (MIT/Apache/etc.) in the repository root.

---

If you'd like the Modrinth description tailored with screenshots, changelog, or donation links (Patreon/Ko-fi), tell me what to include and I'll update it.
