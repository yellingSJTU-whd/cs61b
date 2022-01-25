public class Palindrome {

    /**
     * Convert a String to a deque containing the Characters.
     *
     * @param word the String to be converted
     * @return a deque containing the Characters
     */
    public Deque<Character> wordToDeque(String word) {
        char[] charArray = word.toCharArray();
        Deque<Character> deque = new ArrayDeque<>();
        for (char ch : charArray) {
            deque.addLast(ch);
        }
        return deque;
    }

    /**
     * Determines whether the specified String is a palindrome.
     *
     * @param word the String to be determined.
     * @return false if word is not a palindrome or specially input is null, true otherwise.
     */
    public boolean isPalindrome(String word) {
        if (word == null) {
            return false;
        }
        return isPalindrome(wordToDeque(word));
    }

    private boolean isPalindrome(Deque<Character> deque) {
        int length = deque.size();
        if (length == 0 || length == 1) {
            return true;
        }
        Character first = deque.removeFirst();
        Character last = deque.removeLast();
        return first.equals(last) && isPalindrome(deque);
    }

    /**
     * Determines whether a String is palindrome with given CharacterComparator.
     *
     * @param word the String to be determined
     * @param cc   specified CharacterComparator using when Character equality is considered.
     * @return false if word is not a palindrome or specially input is null, true otherwise.
     */
    public boolean isPalindrome(String word, CharacterComparator cc) {
        if (word == null) {
            return false;
        }
        return isPalindrome(wordToDeque(word), cc);
    }

    private boolean isPalindrome(Deque<Character> deque, CharacterComparator cc) {
        int length = deque.size();
        if (length == 0 || length == 1) {
            return true;
        }
        Character first = deque.removeFirst();
        Character last = deque.removeLast();
        return cc.equalChars(first, last) && isPalindrome(deque, cc);
    }
}
