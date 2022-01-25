import org.junit.Test;

import static org.junit.Assert.*;

public class TestPalindrome {
    // You must use this palindrome, and not instantiate
    // new Palindromes, or the autograder might be upset.
    static Palindrome palindrome = new Palindrome();
    static CharacterComparator cc = new OffByOne();
    static CharacterComparator cc5 = new OffByN(5);

    @Test
    public void testWordToDeque() {
        Deque d = palindrome.wordToDeque("persiflage");
        String actual = "";
        for (int i = 0; i < "persiflage".length(); i++) {
            actual += d.removeFirst();
        }
        assertEquals("persiflage", actual);
    }

    @Test
    public void testIsPalindrome() {
        assertFalse(palindrome.isPalindrome(null));
        assertFalse(palindrome.isPalindrome("abA"));
        assertFalse(palindrome.isPalindrome("whatever"));
        assertTrue(palindrome.isPalindrome("abcdcba"));
        assertTrue(palindrome.isPalindrome("xyzzyx"));
        assertTrue(palindrome.isPalindrome("a"));
        assertTrue(palindrome.isPalindrome(""));
    }

    @Test
    public void testOffByOne() {
        assertFalse(palindrome.isPalindrome(null, cc));
        assertTrue(palindrome.isPalindrome("", cc));
        assertTrue(palindrome.isPalindrome("a", cc));
        assertTrue(palindrome.isPalindrome("flake", cc));
        assertFalse(palindrome.isPalindrome("aabaa", cc));
        assertFalse(palindrome.isPalindrome("flakE", cc));
    }

    @Test
    public void testOffByN() {
        assertFalse(palindrome.isPalindrome(null, cc5));
        assertTrue(palindrome.isPalindrome("", cc5));
        assertTrue(palindrome.isPalindrome("a", cc5));
        assertTrue(palindrome.isPalindrome("bing", cc5));
        assertFalse(palindrome.isPalindrome("binG", cc5));
        assertFalse(palindrome.isPalindrome("aabaa", cc5));
    }
}
