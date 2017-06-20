public class Charray {
    /**
     * Für kleine Strings bei Speicherintensiven
     * Problemen verwenden, verbraucht ca. 40 Byte weniger
     * Speicher als String
     * String: (bytes) = 8 * (int) ((((no chars) * 2) + 45) / 8)
     * Char-Array: 2*no chars +
     */
    char[] character_array;

    public Charray(int length){
        character_array = new char[length];
    }

    public void set(String character_array) {
        for (int i = 0; i < this.character_array.length; i++) {
            this.character_array[i] = character_array.charAt(i);
        }
    }

    public String get() {
        return new String(character_array);
    }

    public String toString() {
        return new String(character_array);
    }
}
