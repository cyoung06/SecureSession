# Secure Session
Secure Session is a forge 1.8.9 mod aiming to enhance session security in Minecraft Forge

## What does it do?
- injects security checks into `getToken()` and `getPlayerID()` in `net.minecraft.util.Session`
- changes field name of session token in `net.minecraft.util.Session` to completely random string
- calls `Reflection.registerFieldsToFilter` to prevent getting field of session token
- injects custom security manager, replacing fml one.  (socket / file permissions in the future?)
- calls `Reflection.registerMethodsToFilter` to prevent internal method `getDeclaredFields0` from being discovered. (if not blocked, it can be used to bypass `registerFieldsToFilter`)

## Notes

Please do note that installing this mod does **NOT** make your minecraft completely safe.
You should still not install random mods from random people, and only download mods from official sources.