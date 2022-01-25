public class OffByN implements CharacterComparator {
    private final int offSet;

    public OffByN(int n) {
        offSet = n;
    }

    @Override
    public boolean equalChars(char x, char y) {
        return Math.abs(x - y) == offSet;
    }
}
