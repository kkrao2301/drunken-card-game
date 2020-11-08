package io.socash.kkolluri.drunkencard.games;

import java.util.List;

import io.socash.kkolluri.drunkencard.exception.DidntDealException;
import io.socash.kkolluri.drunkencard.exception.GameFinalizedException;
import io.socash.kkolluri.drunkencard.exception.GameFullException;
import io.socash.kkolluri.drunkencard.exception.InsufficientPlayersException;
import io.socash.kkolluri.drunkencard.model.Player;

public interface ICardGame {
	boolean addPlayer(String nickName)throws GameFinalizedException, GameFullException;
	boolean deal() throws InsufficientPlayersException, GameFinalizedException;
	String decideWinner() throws DidntDealException,GameFinalizedException;
	List<Player> getPlayers();
}
