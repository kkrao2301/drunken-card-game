package io.socash.kkolluri.drunkencard.model;

import java.util.Optional;

import io.socash.kkolluri.drunkencard.exception.DeckEmptyException;

//A deck is associated with a game and will have 52 cards(No Joker).
//A deck will also provide the functionality to draw a card randomly.
public class Deck {
	private boolean[] cards;
	int drawn;
	
	public Deck() {
		this.cards = new boolean[52];
		drawn = 0;
	}
	
	public Card drawCard() throws DeckEmptyException {
		if (drawn == 52) {
			throw new DeckEmptyException("Deck empty, Please reshuffle and continue");
		}
		int position = -1;
		Card card = null;
		while (position == -1 || this.cards[position]) {
			position = (int) (Math.random() * 52);
		}
		this.cards[position] = true;
		Optional<Suit> oSuit = Suit.valueOf(position / 13);
		Optional<Rank> oRank = Rank.valueOf((position % 13) + 1);
		if (oSuit.isPresent() && oRank.isPresent()) {
			card = new Card(oRank.get(), oSuit.get());
		}
		drawn++;
		return card;
	}

	public void reshuffle() {
		this.cards = new boolean[52];
		drawn = 0;
	}
}
