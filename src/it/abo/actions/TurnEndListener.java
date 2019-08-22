package it.abo.actions;

import it.abo.models.Player;

public interface TurnEndListener {
	
		public abstract void turnEnd(Player player);
}
