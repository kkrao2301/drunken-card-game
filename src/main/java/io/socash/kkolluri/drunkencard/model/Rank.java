package io.socash.kkolluri.drunkencard.model;

import java.util.Arrays;
import java.util.Optional;

//Represents the rank of Card
public enum Rank {
	ACE(1), 
	TWO(2), 
	THREE(3),
	FOUR(4),
	FIVE(5),
	SIX(6),
	SEVEN(7),
	EIGHT(8),
	NINE(9),
	TEN(10),
	JACK(11),
	QUEEN(12),
	KING(13);
	
	private final int value;
	
    private Rank(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
    
    public static Optional<Rank> valueOf(int value) {
        return Arrays.stream(Rank.values())
            .filter(rank -> rank.value == value)
            .findFirst();
    }
}
