# GroupTP Plugin

GroupTP is a versatile Minecraft plugin that allows server administrators to set up group teleportation points and random teleportation functionality. It provides an easy way to manage teleportation buttons for both group and random teleports.

## Features

- Create and manage group teleportation points
- Set up random teleportation (RTP) functionality
- Add and remove teleportation buttons for both group and random teleports
- Reload configuration
- Admin tools for easy setup and management

## Commands

### Group Teleport Commands
Permission: `grouptp.admin`

| Command | Description |
|---------|-------------|
| `/grouptp create <name> <amount>` | Creates a new group teleport point |
| `/grouptp setbutton gtp <name>` | Sets a button for an existing group teleport |
| `/grouptp removebutton gtp <name>` | Removes a group teleport button |
| `/grouptp item` | Gives the player a selector item for defining teleport areas |
| `/grouptp reload` | Reloads the plugin configuration |

### Random Teleport (RTP) Commands

| Command | Description | Permission |
|---------|-------------|------------|
| `/grouptp setbutton rtp` | Sets a button for random teleportation | `grouptp.admin` |
| `/grouptp removebutton rtp` | Removes a random teleport button | `grouptp.admin` |
| `/randomtp` or `/rtp` | Teleports the player to a random location | `randomtp.use` |

## Usage

1. **Setting up a Group Teleport**
   - Use the selector item to define the teleport area
   - Create the group teleport with `/grouptp create <name> <amount>`
   - Set up buttons for the teleport with `/grouptp setbutton gtp <name>`

2. **Setting up Random Teleport**
   - Set up RTP buttons around your server using `/grouptp setbutton rtp`
   - Players with appropriate permissions can use `/rtp` to teleport randomly

3. **Managing Teleport Buttons**
   - Remove unwanted buttons using the respective remove commands
   - Reload the configuration after making changes with `/grouptp reload`

## Configuration

Here's an example of the plugin's configuration file:

```yaml
cooldown: 5
cooldownMessage: "&cYou must wait {sec} seconds!"
notEnoughMembersMessage: "&cCannot teleport. Required {members-size} member(s)"
cannotUseRandomTP: "&cYou cannot use /rtp in this world"
safeLocationNotFound: "&cFailed to find a safe location"
teleportData:
  world:
    minRadius: 500
    maxRadius: 1000
  world_the_end:
    minRadius: 100
    maxRadius: 300
disabledMaterials:
  - "LAVA"
  - "WATER"
rtpLocations: []
teleports: []
```

| Config Option | Description |
|---------------|-------------|
| `cooldown` | Time in seconds between teleports |
| `cooldownMessage` | Message shown when a player is on cooldown |
| `notEnoughMembersMessage` | Message shown when there aren't enough players for group teleport |
| `cannotUseRandomTP` | Message shown when RTP is disabled in a world |
| `safeLocationNotFound` | Message shown when no safe location is found for teleportation |
| `teleportData` | Configures min and max teleport radius for different worlds |
| `disabledMaterials` | List of materials that are considered unsafe for teleportation |
| `rtpLocations` | List of RTP button locations (automatically populated) |
| `teleports` | List of group teleports (automatically populated) |

## Installation

1. Download the GroupTP plugin JAR file.
2. Place the JAR file in your server's `plugins` folder.
3. Restart your server or use a plugin manager to load the plugin.
4. Configure the plugin settings in the `config.yml` file generated in the `plugins/GroupTP` folder.

## Support

If you encounter any issues or have questions about the GroupTP plugin, please open an issue on this GitHub repository.
