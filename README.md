# Hytale Plugin Template for VSCode and Gradle

This is a Work in Progress and refers to Hytale Modding's Template.

> [!WARNING]
> This project assumes use of VSCode Devcontainers and Gradle. Steps may vary for IntelliJ. Please refer to https://hytalemodding.dev/en/docs/guides/plugin/setting-up-env

## Setup

Open VSCode Terminal and execute: `sh ./update-api.sh`. This script will execute the Hytale Downloader and load the HytaleServer.jar to the correct path. You will need to authenticate with Hytale during this process. Please ensure you NEVER share `.hytale-downloader-credentials.json` to other people or via git.

Once the Hytale Downloader is complete, access the Command Palette (Default: `CTRL+SHIFT+P`) and use `>java: reload projects` to initiate a reload.

## Update Manifest

Navigate to `src/main/resources/manifest.json` and update it to reflect your plugin. You will need to rename the package path folders based on changes you make here.

## Build

Open VSCode Terminal and execute: `gradle build`. That's it! You will have `build/libs/ExamplePlugin-1.0.0-SNAPSHOT.jar` which can be placed in your Local Development Server Mods Folder.