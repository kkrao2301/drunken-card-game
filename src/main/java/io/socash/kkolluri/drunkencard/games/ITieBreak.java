package io.socash.kkolluri.drunkencard.games;

import java.util.List;

import io.socash.kkolluri.drunkencard.model.Player;

public interface ITieBreak {
	String tieBreak(List<Player> tied);
}
