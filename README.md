<p align="center">
    <img src="./logo.png">
</p>

<p align="center">
    <a href="https://modrinth.com/mod/one-time-overrides">
      <img width="160" src="https://raw.githubusercontent.com/modrinth/art/main/Branding/Badge/badge-dark.svg">
    </a>
</p>

<p align="center">
    <b>A Fabric mod allowing modpack authors to add non-overwriting override files.</b>
</p>

# OneTimeOverrides
Some override files, such as `options.txt`, should only be copied the first time a player plays a modpack.
Otherwise, their changes would be lost every time the modpack is updated.

This mod allows modpack authors to place files in the `one-time-overrides` directory
that will only be copied to their intended destination if the target file does not exist.

Example:
```
.minecraft
├── config
│   └── first_mod.toml
└── one-time-overrides
    │   └── config
    │       └── first_mod.toml  # already exists in .minecraft/config -> will not be copied
    └── options.txt  # does not exist in .minecraft -> will be copied
```

As it does not use any Minecraft-specific APIs, the mod should work with any version of the game.
`1.18.2` and `1.19.2` were tested.

The only dependency is [fabric-language-kotlin](https://github.com/FabricMC/fabric-language-kotlin).
[Fabric API](https://github.com/FabricMC/fabric) is not required.
