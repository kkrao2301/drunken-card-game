package io.socash.kkolluri.drunkencard.games;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.socash.kkolluri.drunkencard.exception.DeckEmptyException;
import io.socash.kkolluri.drunkencard.exception.DidntDealException;
import io.socash.kkolluri.drunkencard.exception.GameFinalizedException;
import io.socash.kkolluri.drunkencard.model.Card;
import io.socash.kkolluri.drunkencard.model.Player;

public class DrunkenCardGame extends CardGame implements ITieBreak{
	Logger logger = LoggerFactory.getLogger(DrunkenCardGame.class);
	public DrunkenCardGame() {
		super(4, 3);
	}

	@Override
	public String decideWinner() throws DidntDealException,GameFinalizedException{
		if (!this.isDealt()) {
			throw new DidntDealException("Please deal the cards");
		}
		
		if (this.isFinalized()) {
			throw new GameFinalizedException("Game already finalized");
		}
		List<Player> players = this.getPlayers();
		logger.info("Deciding game");
		for(Player player : players) {
			logger.info(player.getNickName());
			for (Card card : player.getHand()) {
				logger.info(card.getRank().name());
			}
		}
		players.sort((a, b) -> comparePlayers(b, a));
		if (comparePlayers(players.get(0), players.get(1)) != 0) {
			this.setFinalized(true);
			return players.get(0).getNickName();
		}
		List<Player> tied = getTiedFromMain(players);
		return tieBreak(tied);
		
	}
	
	@Override
	public String tieBreak(List<Player> tied) {
		logger.info("Deciding Tie");
		while(tied.size() > 1) {
			for(Player player : tied) {
				logger.info(player.getNickName());
				for (Card card : player.getHand()) {
					logger.info(card.getRank().name());
				}
			}
			for (int i = 0; i < tied.size(); i++) {
				tied.get(i).setHand(new ArrayList<Card>());
				try {
					tied.get(i).getHand().add(this.getDeck().drawCard());
				} catch (DeckEmptyException e) {
					this.getDeck().reshuffle();
					i = -1;
				}
			}
			tied.sort((a,b) -> compareByMax(b.getHand(), a.getHand()));
			if (compareByMax(tied.get(0).getHand(), tied.get(1).getHand()) == 0) {
				tied = getTiedFromTieBreak(tied);
			} else {
				this.setFinalized(true);
				return tied.get(0).getNickName();
			}
		}
		this.setFinalized(true);
		return tied.get(0).getNickName();
	}

	private List<Player> getTiedFromTieBreak(List<Player> tied) {
		int max = findMax(tied.get(0).getHand());
		return tied.stream()
					.filter(a -> findMax(a.getHand()) == max)
					.collect(Collectors.toList());
	}

	private List<Player> getTiedFromMain(List<Player> players) {
		List<Player> tied = new ArrayList<Player>();
		tied.add(players.get(0));
		tied.add(players.get(1));
		if (comparePlayers(players.get(0), players.get(2)) != 0)
			tied.add(players.get(2));
		if (comparePlayers(players.get(0), players.get(3)) != 0)
			tied.add(players.get(3));
		return tied;
	}

	private int comparePlayers(Player a, Player b) {
		return this.compareHands(a.getHand(), b.getHand());
	}

	private int compareHands(List<Card> hand1, List<Card> hand2) {
		int byTrail = this.compareByTrail(hand1, hand2);
		if (byTrail != 0) {
			return byTrail;
		}
		
		int bySequence = this.compareBySequence(hand1, hand2);
		if (bySequence != 0) {
			return bySequence;
		}
		
		int byPair = this.compareByPair(hand1, hand2);
		if (byPair != 0) {
			return byPair;
		}
		
		int byMax = this.compareByMax(hand1, hand2);
		if (byMax != 0) {
			return byMax;
		}
		
		return 0;
	}

	private int compareByTrail(List<Card> hand1, List<Card> hand2) {
		int hand1Trail = this.findTrail(hand1);
		int hand2Trail = this.findTrail(hand2);
		if (hand1Trail == -1 && hand2Trail == -1) {
			return 0;
		}
		
		if (hand1Trail != -1 || hand2Trail != -1) {
			if (hand1Trail == -1) {
				return -1;
			} 
			if (hand2Trail == -1) {
				return 1;
			}
		}
		return hand1Trail - hand2Trail;
	}

	private int findTrail(List<Card> hand) {
		int trail = -1;
		if (hand.get(0).getRank() == hand.get(1).getRank() 
			&& hand.get(1).getRank() == hand.get(2).getRank()) {
			int rank = hand.get(0).getRank().getValue();
			trail = rank == 1 ? 14 : rank;
		}
		
		return trail;
	}
	
	private int compareBySequence(List<Card> hand1, List<Card> hand2) {
		int hand1Sequence = this.findSequence(hand1);
		int hand2Sequence = this.findSequence(hand2);
		if (hand1Sequence == -1 && hand2Sequence == -1) {
			return 0;
		}
		
		if (hand1Sequence != -1 || hand2Sequence != -1) {
			if (hand1Sequence == -1) {
				return -1;
			} 
			if (hand2Sequence == -1) {
				return 1;
			}
		}
		return hand1Sequence - hand2Sequence;
	}

	private int findSequence(List<Card> hand) {
		List<Integer> values = hand.stream()
								.map(a -> a.getRank().getValue())
								.sorted((a, b) -> a - b)
								.collect(Collectors.toList());
		if (values.get(0) == 1 && (values.get(1) == 12 && values.get(2) == 13)) {
			return 14;
		}
		
		int sequence = -1;
		if (values.get(1) - values.get(0) == 1
			&& values.get(2) - values.get(1) == 1)
			sequence = values.get(2);
		return sequence;
	}
	
	private int compareByPair(List<Card> hand1, List<Card> hand2) {
		int hand1Pair = this.findPair(hand1);
		int hand2Pair = this.findPair(hand2);
		
		if (hand1Pair == -1 && hand2Pair == -1) {
			return 0;
		}
		
		if (hand1Pair != -1 || hand2Pair != -1) {
			if (hand1Pair == -1) {
				return -1;
			} 
			if (hand2Pair == -1) {
				return 1;
			}
		}
		return hand1Pair - hand2Pair;
	}

	private int findPair(List<Card> hand) {
		List<Integer> values = hand.stream()
				.map(a -> a.getRank().getValue())
				.sorted((a, b) -> a -b)
				.collect(Collectors.toList());
		int pair = -1;
		if (values.get(0) == values.get(1)
			|| values.get(1) == values.get(2)) {
			int rank = values.get(1);
			pair = rank == 1 ? 14 : rank;
		}
		return pair;
	}
	
	private int compareByMax(List<Card> hand1, List<Card> hand2) {
		int hand1Max = findMax(hand1);
		int hand2Max = findMax(hand2);
		return hand1Max - hand2Max;
	}

	private int findMax(List<Card> hand) {
		List<Integer> values = hand.stream()
				.map(a -> a.getRank().getValue())
				.sorted((a, b) -> b - a)
				.collect(Collectors.toList());
		int max = values.get(0);
		if (values.get(values.size() - 1) == 1) {
			max = 14;
		}
		return max;
	}
}
