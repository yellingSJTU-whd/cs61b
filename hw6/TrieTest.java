import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.*;

class TrieTest {

    @ParameterizedTest
    @MethodSource("dictBuilder")
    public void testTrie(String str) {
        Trie trie = new Trie(dictBuilder());
        assertTrue(trie.contained(str));
    }

    private static String[] dictBuilder() {
        In in = new In(Boggle.dictPath);
        return in.readAllStrings();
    }
}
