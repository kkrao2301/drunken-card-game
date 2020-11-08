package io.socash.kkolluri.drunkencard.games;

import java.util.ArrayList;
import java.util.List;

import io.socash.kkolluri.drunkencard.exception.DeckEmptyException;
import io.socash.kkolluri.drunkencard.exception.GameFinalizedException;
import io.socash.kkolluri.drunkencard.exception.GameFullException;
import io.socash.kkolluri.drunkencard.exception.InsufficientPlayersException;
import io.socash.kkolluri.drunkencard.model.Card;
import io.socash.kkolluri.drunkencard.model.Deck;
import io.socash.kkolluri.drunkencard.model.Player;

public abstract class CardGame implements ICardGame{
	private Deck deck;
	private List<Player> players;
	
	private boolean ready = false;
	private boolean dealt = false;
	private boolean finalized = false;
	
	private int handCount;
	private int playerCount;
	
	public CardGame(int playerCount, int handCount) {
		this.playerCount = playerCount;
		this.handCount = handCount;
		this.deck = new Deck();
		this.players = new ArrayList<Player>();
	}
	
	public boolean addPlayer(String nickName) throws GameFinalizedException, GameFullException{
		if (this.isFinalized()) {
			throw new GameFinalizedException("Game completed already, please join a new game");
		}
		if (players.size() == playerCount) {
			throw new GameFullException("Game player limit reached");
		}
		Player player = new Player(nickName);
		this.players.add(player);
		if (this.players.size() == playerCount) {
			this.ready = true;
		}
		return true;
	}
	
	public boolean deal() throws InsufficientPlayersException, GameFinalizedException {
		if (this.isDealt()) {
			throw new GameFinalizedException("Game already completed");
		}
		if (!this.isReady()) {
			throw new InsufficientPlayersException("Current players are less than the required count, please invite your friends");
		}
		
		for (int i = 0; i < this.playerCount; i++) {
			this.players.get(i).setHand(new ArrayList<Card>());
			for (int j = 0; j < this.handCount; j++) {
				Card card;
				try {
					card = this.deck.drawCard();
					this.players.get(i).getHand().add(card);
				} catch (DeckEmptyException e) {
					this.deck.reshuffle();
					i = -1;
				}
				
			}
		}
		
		this.dealt = true;
		return true;
	}

	public Deck getDeck() {
		return deck;
	}

	public void setDeck(Deck deck) {
		this.deck = deck;
	}

	public List<Player> getPlayers() {
		return players;
	}

	public void setPlayers(List<Player> players) {
		this.players = players;
	}

	public boolean isReady() {
		return ready;
	}

	public void setReady(boolean ready) {
		this.ready = ready;
	}

	public int getHandCount() {
		return handCount;
	}

	public void setHandCount(int handCount) {
		this.handCount = handCount;
	}

	public int getPlayerCount() {
		return playerCount;
	}

	public void setPlayerCount(int playerCount) {
		this.playerCount = playerCount;
	}

	public boolean isDealt() {
		return dealt;
	}

	public void setDealt(boolean dealt) {
		this.dealt = dealt;
	}

	public boolean isFinalized() {
		return finalized;
	}

	public void setFinalized(boolean finalized) {
		this.finalized = finalized;
	}
}
