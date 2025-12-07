package yorick.gg.chestLog

import java.io.File
import org.bukkit.plugin.java.JavaPlugin
import yorick.gg.chestLog.listeners.ChestListener

class ChestLog : JavaPlugin() {

    override fun onEnable() {
        server.pluginManager.registerEvents(ChestListener(this), this)
        logger.info("ChestLog enabled successfully!")

        val pluginFolder: File = dataFolder.parentFile
        val folderName: String = "ChestLogs"
        var newFolder = File(pluginFolder, folderName)

        if (!newFolder.exists()) {
            try {
            newFolder.mkdir()
            logger.info("Folder ${newFolder.absolutePath} created")
            } catch (e: SecurityException) {
            logger.severe("Failed to create folder ${newFolder.absolutePath}")
            e.printStackTrace()
            }
        }
        
        val logsFile = File(newFolder, "chestlogs.log")
        if (!logsFile.exists()) {
            logsFile.createNewFile()
        }
    }
}
