import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class DefferedTeleportation implements Runnable {
	private Map<Player, Object> playerTeleportation = new HashMap<Player, Object>();

	public void teleport(Player p, Location l) {
		synchronized (playerTeleportation) {
			playerTeleportation.put(p, l);
		}
	}

	public void teleport(Player p, Entity l) {
		synchronized (playerTeleportation) {
			playerTeleportation.put(p, l);
		}
	}

	@Override
	public void run() {
		synchronized (playerTeleportation) {
			Set<Entry<Player, Object>> entries = playerTeleportation.entrySet();
			for (Entry<Player, Object> ent : entries) {
				if (ent.getValue() != null && ent.getKey().isValid()
						&& ent.getKey().isOnline()) {
					if (ent.getValue() instanceof Entity) {
						ent.getKey().teleport((Entity) ent.getValue());
					} else if (ent.getValue() instanceof Location) {
						ent.getKey().teleport((Location) ent.getValue());
					}
				}
			}
			playerTeleportation.clear();
		}
	}
}