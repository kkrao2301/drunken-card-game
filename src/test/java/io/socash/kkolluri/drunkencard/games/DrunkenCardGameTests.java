package io.socash.kkolluri.drunkencard.games;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import io.socash.kkolluri.drunkencard.exception.DidntDealException;
import io.socash.kkolluri.drunkencard.exception.GameFinalizedException;
import io.socash.kkolluri.drunkencard.exception.GameFullException;
import io.socash.kkolluri.drunkencard.exception.InsufficientPlayersException;
import io.socash.kkolluri.drunkencard.model.Card;
import io.socash.kkolluri.drunkencard.model.Rank;
import io.socash.kkolluri.drunkencard.model.Suit;

@SpringBootTest
public class DrunkenCardGameTests {
	@Test
	void checkGameScene() throws GameFinalizedException, GameFullException, InsufficientPlayersException, DidntDealException {
		DrunkenCardGame game = new DrunkenCardGame();
		assertEquals(3, game.getHandCount());
		assertEquals(4, game.getPlayerCount());
		assertEquals(false, game.isReady());
		assertEquals(false, game.isDealt());
		assertEquals(false, game.isFinalized());
		game.addPlayer("Player1");
		game.addPlayer("Player2");
		game.addPlayer("Player3");
		assertEquals(false, game.isReady());
		game.addPlayer("Player4");
		assertEquals(true, game.isReady());
		game.deal();
		assertEquals(true, game.isDealt());
		assertEquals(false, game.isFinalized());
		game.decideWinner();
		assertEquals(true, game.isFinalized());
	}
	
	@Test
	void checkTrail() throws GameFinalizedException, GameFullException, DidntDealException {
		DrunkenCardGame game = new DrunkenCardGame();
		game.addPlayer("Player1");
		game.addPlayer("Player2");
		game.addPlayer("Player3");
		game.addPlayer("Player4");
		game.getPlayers().get(0).setHand(getTrail(Rank.ACE));
		game.getPlayers().get(1).setHand(getTrail(Rank.KING));
		game.getPlayers().get(2).setHand(getTrail(Rank.EIGHT));
		game.getPlayers().get(3).setHand(getTrail(Rank.FIVE));
		game.setDealt(true);
		game.setReady(true);
		assertEquals("Player1", game.decideWinner());
		
		game = new DrunkenCardGame();
		game.addPlayer("Player1");
		game.addPlayer("Player2");
		game.addPlayer("Player3");
		game.addPlayer("Player4");
		game.getPlayers().get(0).setHand(getTrail(Rank.TWO));
		game.getPlayers().get(1).setHand(getSequence(Rank.ACE));
		game.getPlayers().get(2).setHand(getSequence(Rank.EIGHT));
		game.getPlayers().get(3).setHand(getPair(Rank.FIVE));
		game.setDealt(true);
		game.setReady(true);
		assertEquals("Player1", game.decideWinner());
	}
	
	@Test
	void checkSequence() throws GameFinalizedException, GameFullException, DidntDealException {
		DrunkenCardGame game = new DrunkenCardGame();
		game.addPlayer("Player1");
		game.addPlayer("Player2");
		game.addPlayer("Player3");
		game.addPlayer("Player4");
		game.getPlayers().get(0).setHand(getSequence(Rank.FIVE));
		game.getPlayers().get(1).setHand(getSequence(Rank.KING));
		game.getPlayers().get(2).setHand(getSequence(Rank.EIGHT));
		game.getPlayers().get(3).setHand(getSequence(Rank.ACE));
		game.setDealt(true);
		game.setReady(true);
		assertEquals("Player4", game.decideWinner());
		
		game = new DrunkenCardGame();
		game.addPlayer("Player1");
		game.addPlayer("Player2");
		game.addPlayer("Player3");
		game.addPlayer("Player4");
		game.getPlayers().get(0).setHand(getSequence(Rank.FOUR));
		game.getPlayers().get(1).setHand(getPair(Rank.ACE));
		game.getPlayers().get(2).setHand(getPair(Rank.KING));
		game.getPlayers().get(3).setHand(getPair(Rank.QUEEN));
		game.setDealt(true);
		game.setReady(true);
		assertEquals("Player1", game.decideWinner());
	}
	
	@Test
	void checkPair() throws GameFinalizedException, GameFullException, DidntDealException {
		DrunkenCardGame game = new DrunkenCardGame();
		game.addPlayer("Player1");
		game.addPlayer("Player2");
		game.addPlayer("Player3");
		game.addPlayer("Player4");
		game.getPlayers().get(0).setHand(getPair(Rank.FIVE));
		game.getPlayers().get(1).setHand(getPair(Rank.ACE));
		game.getPlayers().get(2).setHand(getPair(Rank.EIGHT));
		game.getPlayers().get(3).setHand(getPair(Rank.KING));
		game.setDealt(true);
		game.setReady(true);
		assertEquals("Player2", game.decideWinner());
		
		game = new DrunkenCardGame();
		game.addPlayer("Player1");
		game.addPlayer("Player2");
		game.addPlayer("Player3");
		game.addPlayer("Player4");
		game.getPlayers().get(0).setHand(getPair(Rank.FIVE));
		game.getPlayers().get(1).setHand(getMax(Rank.ACE));
		game.getPlayers().get(2).setHand(getMax(Rank.EIGHT));
		game.getPlayers().get(3).setHand(getMax(Rank.KING));
		game.setDealt(true);
		game.setReady(true);
		assertEquals("Player1", game.decideWinner());
	}

	void checkMax() throws GameFinalizedException, GameFullException, DidntDealException {
		DrunkenCardGame game = new DrunkenCardGame();
		game.addPlayer("Player1");
		game.addPlayer("Player2");
		game.addPlayer("Player3");
		game.addPlayer("Player4");
		game.getPlayers().get(0).setHand(getMax(Rank.QUEEN));
		game.getPlayers().get(1).setHand(getMax(Rank.KING));
		game.getPlayers().get(2).setHand(getMax(Rank.ACE));
		game.getPlayers().get(3).setHand(getMax(Rank.EIGHT));
		game.setDealt(true);
		game.setReady(true);
		assertEquals("Player3", game.decideWinner());
	}
	
	
	private List<Card> getTrail(Rank rank) {
		List<Card> hand = new ArrayList<>();
		hand.add(new Card(rank, Suit.CLUBS));
		hand.add(new Card(rank, Suit.SPADES));
		hand.add(new Card(rank, Suit.DIAMONDS));
		return hand;
	}
	
	private List<Card> getSequence(Rank rank) {
		int value = rank.getValue();
		List<Card> hand = new ArrayList<>();
		if (rank == Rank.ACE) {
			hand.add(new Card(rank.ACE, Suit.CLUBS));
			hand.add(new Card(rank.KING, Suit.SPADES));
			hand.add(new Card(rank.QUEEN, Suit.DIAMONDS));
			return hand;
		}
		
		hand.add(new Card(Rank.valueOf(value).get(), Suit.CLUBS));
		hand.add(new Card(Rank.valueOf(value - 1).get(), Suit.SPADES));
		hand.add(new Card(Rank.valueOf(value - 2).get(), Suit.DIAMONDS));
		return hand;
	}
	
	private List<Card> getPair(Rank rank) {
		int value = rank.getValue();
		List<Card> hand = new ArrayList<>();
		if (rank == Rank.ACE) {
			hand.add(new Card(rank.ACE, Suit.CLUBS));
			hand.add(new Card(rank.ACE, Suit.SPADES));
			hand.add(new Card(rank.QUEEN, Suit.DIAMONDS));
			return hand;
		}
		
		hand.add(new Card(Rank.valueOf(value).get(), Suit.CLUBS));
		hand.add(new Card(Rank.valueOf(value).get(), Suit.SPADES));
		hand.add(new Card(Rank.valueOf(value - 2).get(), Suit.DIAMONDS));
		return hand;
	}
	
	private List<Card> getMax(Rank rank) {
		int value = rank.getValue();
		List<Card> hand = new ArrayList<>();
		if (rank == Rank.ACE) {
			hand.add(new Card(rank.ACE, Suit.CLUBS));
			hand.add(new Card(rank.QUEEN, Suit.SPADES));
			hand.add(new Card(rank.TEN, Suit.DIAMONDS));
			return hand;
		}
		
		hand.add(new Card(Rank.valueOf(value).get(), Suit.CLUBS));
		hand.add(new Card(Rank.valueOf(value - 2).get(), Suit.SPADES));
		hand.add(new Card(Rank.valueOf(value - 4).get(), Suit.DIAMONDS));
		return hand;
	}
}
