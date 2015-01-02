public enum Tile {
	EMPTY ("Empty"),
	AIR ("Air"),
	EARTH ("Earth"),
	FIRE ("Fire"),
	WATER ("Water"),
	COIN ("Coin"),
	STAR ("Star"),
	SKULL ("Skull"),
	SKULL5 ("Skull5"),
	WILD2 ("Wild2"),
	WILD3 ("Wild3"),
	WILD4 ("Wild4"),
	WILD5 ("Wild5"),
	WILD6 ("Wild6"),
	WILD7 ("Wild7"),
	WILD8 ("Wild8");

	private final String description;

	private Tile(String description) {
		this.description = description;
	}

	private boolean isSkull() {
		return this == SKULL || this == SKULL5;
	}

	private boolean isElement() {
		return this == AIR || this == EARTH || this == FIRE || this == WATER;
	}

	private boolean isWild() {
		return this == WILD2 || this == WILD3 || this == WILD4 || this == WILD5 ||
				this == WILD6 || this == WILD7 || this == WILD8;
	}

	public boolean matches(Tile otherTile) {
		// TODO(ebensh): Precompute this by creating a matches[this][otherTile]
		// 2d boolean array.
		if (this == otherTile) { return true; }
		if (this.isSkull() && otherTile.isSkull()) { return true; }
		if (this.isElement() && otherTile.isWild() ||
				this.isWild() && otherTile.isElement() ||
				this.isWild() && otherTile.isWild()) { return true; }
		return false;
	}

	public String toString() { return this.description; }
}

