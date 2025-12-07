package yorick.gg.chestLog.listeners

import org.bukkit.Material
import org.bukkit.entity.HumanEntity
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.inventory.InventoryAction
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryType
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.Inventory
import yorick.gg.chestLog.ChestLog
import kotlin.math.roundToInt
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.io.File

class ChestListener(private val plugin: ChestLog) : Listener {

    @EventHandler
    fun onMoveItem(event: InventoryClickEvent) {
        
        val player: HumanEntity = event.whoClicked
        val playerLoc = player.location
        val dateAndTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-YYYY HH:mm"))
        val logFile: File = File("${plugin.dataFolder.parentFile}/ChestLogs/chestlogs.log")

        data class PlayerInfo(
            val name: String,
            val x: Int,
            val y: Int,
            val z: Int,
            val newItem: Material?,
            var itemAmount: Int?
        )

        val playerInfo = PlayerInfo(
            name = event.whoClicked.name,
            x = playerLoc.x.roundToInt(),
            y = playerLoc.y.roundToInt(),
            z = playerLoc.z.roundToInt(),
            newItem = event.currentItem?.type,
            itemAmount = event.currentItem?.amount
        )

        data class InventoryInfo(
            val clickedInv: InventoryType?,
            val invType: InventoryType
        )

        val inventoryInfo = InventoryInfo(
            clickedInv = event.clickedInventory?.type,
            invType = event.view.topInventory.type
        )

        if (!event.inventory.type.equals(InventoryType.CHEST)) return
        if (inventoryInfo.clickedInv != inventoryInfo.invType) return
        if (event.action != InventoryAction.PLACE_ALL && event.action != InventoryAction.MOVE_TO_OTHER_INVENTORY) return
        
        val newLine:String = "${playerInfo.name} grabbed item: ${playerInfo.newItem} (${playerInfo.itemAmount}) from a chest at X:${playerInfo.x} Y:${playerInfo.z} Z:${playerInfo.z} ($dateAndTime)\n" 

        if (logFile.exists()) {
            logFile.appendText(newLine)
        }
    }
}
