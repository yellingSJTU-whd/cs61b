import org.junit.Test;

import static org.junit.Assert.*;

public class TestPalindrome {
    // You must use this palindrome, and not instantiate
    // new Palindromes, or the autograder might be upset.
    static Palindrome palindrome = new Palindrome();

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
        String nonPalindrome = "whatever";
        String palindrome = "abcdcba";
        String evenPalindrome = "xyzzyx";
        String singleChar = "a";
        String zeroChar = "";

        Palindrome p = new Palindrome();

        assertFalse(p.isPalindrome(null));
        assertFalse(p.isPalindrome(nonPalindrome));
        assertTrue(p.isPalindrome(palindrome));
        assertTrue(p.isPalindrome(evenPalindrome));
        assertTrue(p.isPalindrome(singleChar));
        assertTrue(p.isPalindrome(zeroChar));
    }
}
