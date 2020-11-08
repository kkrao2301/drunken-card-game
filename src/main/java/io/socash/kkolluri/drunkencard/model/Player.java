package io.socash.kkolluri.drunkencard.model;

import java.util.ArrayList;
import java.util.List;

public class Player {
	private String nickName;
	private List<Card> hand;
	
	public Player(String nickName) {
		this.nickName = nickName;
		this.hand = new ArrayList<Card>();
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public List<Card> getHand() {
		return hand;
	}

	public void setHand(List<Card> hand) {
		this.hand = hand;
	}
}
