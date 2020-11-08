package io.socash.kkolluri.drunkencard.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.socash.kkolluri.drunkencard.exception.DidntDealException;
import io.socash.kkolluri.drunkencard.exception.GameFinalizedException;
import io.socash.kkolluri.drunkencard.exception.GameFullException;
import io.socash.kkolluri.drunkencard.exception.InsufficientPlayersException;
import io.socash.kkolluri.drunkencard.games.ICardGame;
import io.socash.kkolluri.drunkencard.model.Player;
import io.socash.kkolluri.drunkencard.repo.CardGameInMemRepo;

@Service
public class DrunkenCardService {
	@Autowired
	private CardGameInMemRepo repo;

	public int createGame(String nickName) {
		int id;
		try {
			id = repo.createGame(nickName);
		} catch (GameFinalizedException e) {
			id = -1;
		} catch (GameFullException e) {
			id = -1;
		}
		return id;
	}

	public ICardGame getGameById(int id) {
		return repo.findById(id);
	}

	public List<Player> joinGame(int id, String nickName) {
		List<Player> players;
		try {
			players = repo.addPlayer(id, nickName);
		} catch (GameFinalizedException | GameFullException e) {
			e.printStackTrace();
			return null;
		}
		return players;
		
	}

	public List<Player> deal(int id) throws InsufficientPlayersException, GameFinalizedException {
		ICardGame game = repo.findById(id);
		game.deal();
		return game.getPlayers();
	}

	public String decideWinner(int id) throws DidntDealException, GameFinalizedException {
		ICardGame game = repo.findById(id);
		String winner = game.decideWinner();
		return winner;
	}
}
