package io.socash.kkolluri.drunkencard.repo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import io.socash.kkolluri.drunkencard.exception.GameFinalizedException;
import io.socash.kkolluri.drunkencard.exception.GameFullException;
import io.socash.kkolluri.drunkencard.games.DrunkenCardGame;
import io.socash.kkolluri.drunkencard.games.ICardGame;
import io.socash.kkolluri.drunkencard.model.Player;

@Component
public class CardGameInMemRepo{
	private Map<Integer,ICardGame> games = new HashMap<Integer, ICardGame>();
	
	public int createGame(String nickName) throws GameFinalizedException, GameFullException {
		ICardGame game = new DrunkenCardGame();
		game.addPlayer(nickName);
		int id = games.size();
		games.put(id, game);
		return id;
	}
	
	public ICardGame findById(int id) {
		if (games.containsKey(id)) {
			return games.get(id);
		}
		return null;
	}
	
	public List<Player> addPlayer(int id, String nickName) throws GameFinalizedException, GameFullException {
		if (games.containsKey(id)) {
			games.get(id).addPlayer(nickName);
			return games.get(id).getPlayers();
		}
		return null;
	}
}
