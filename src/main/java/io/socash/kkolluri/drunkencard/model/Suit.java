package io.socash.kkolluri.drunkencard.model;

import java.util.Arrays;
import java.util.Optional;

//Represents the suit of Card
public enum Suit {
	CLUBS(0),
	SPADES(1),
	HEARTS(2),
	DIAMONDS(3);
	
	private final int value;
	
    private Suit(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
    
    public static Optional<Suit> valueOf(int value) {
        return Arrays.stream(Suit.values())
            .filter(suit -> suit.value == value)
            .findFirst();
    }
}
