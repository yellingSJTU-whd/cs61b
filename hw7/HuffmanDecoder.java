import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class HuffmanDecoder {
    public static void main(String[] args) {
        if (args.length < 2) {
            throw new IllegalArgumentException("illegal argument");
        }
        String encodedFile = args[0];
        String decodedFile = args[1];
        /*
         1: Read the Huffman coding trie.
         */
        ObjectReader reader = new ObjectReader(encodedFile);
        BinaryTrie trie = (BinaryTrie) reader.readObject();

        /*
         2: If applicable, read the number of symbols.
         */
        Integer symbolNum = (Integer) reader.readObject();

        /*
         3: Read the massive bit sequence corresponding to the original txt.
         */
        BitSequence bitSequence = (BitSequence) reader.readObject();

        /*
         4: Repeat until there are no more symbols:
            4a: Perform a longest prefix match on the massive sequence.
            4b: Record the symbol in some data structure.
            4c: Create a new bit sequence containing the remaining unmatched bits.
         */
        int length = bitSequence.length();
//        char[] chars = new char[length];
        List<Character> chars = new ArrayList<>(length);
        int i = 0;
        while (length > 0) {
            Match match = trie.longestPrefixMatch(bitSequence);
            chars.add(match.getSymbol());
//            chars[i++] = match.getSymbol();
            int matchSize = match.getSequence().length();
            bitSequence = bitSequence.allButFirstNBits(matchSize);
            length -= matchSize;
        }

        /*
         5: Write the symbols in some data structure to the specified file.
         */
        char[] array = new char[chars.size()];
        IntStream.range(0, chars.size()).forEachOrdered(j -> array[j] = chars.get(j));
        FileUtils.writeCharArray(decodedFile, array);
    }
}
