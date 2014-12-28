public enum Tile {
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
  WILD8 ("Wild8"),
  WILD9 ("Wild9");
  
  private final String description;
  
  private Tile(String description) {
    this.description = description;
  }
  
  public String toString() { return this.description; }
}

