import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HuffmanEncoder {
    public static Map<Character, Integer> buildFrequencyTable(char[] inputSymbols) {
        Map<Character, Integer> map = new HashMap<>();
        for (char ch : inputSymbols) {
            Integer count = map.getOrDefault(ch, 0);
            map.put(ch, count + 1);
        }
        return map;
    }

    public static void main(String[] args) {
        if (args.length == 0) {
            throw new IllegalArgumentException("Missing filename");
        }
        /*
         1: Read the file as 8 bit symbols.
         */
        String inputFile = args[0];
        char[] input = FileUtils.readFile(inputFile);

        /*
         2: Build frequency table.
         */
        Map<Character, Integer> frequencyTable = buildFrequencyTable(input);

        /*
         3: Use frequency table to construct a binary decoding trie.
         */
        BinaryTrie trie = new BinaryTrie(frequencyTable);

        /*
         4: Write the binary decoding trie to the .huf file.
         */
        String outputFile = inputFile + ".huf";
        ObjectWriter writer = new ObjectWriter(outputFile);
        writer.writeObject(trie);

        /*
         5: (optional: write the number of symbols to the .huf file)
         */
        writer.writeObject(frequencyTable.size());

        /*
         6: Use binary trie to create lookup table for encoding.
         */
        Map<Character, BitSequence> lookupTable = trie.buildLookupTable();

        /*
         7: Create a list of bitsequences.
         */
        List<BitSequence> sequences = new ArrayList<>();

        /*
         8: For each 8 bit symbol:
            Lookup that symbol in the lookup table.
            Add the appropriate bit sequence to the list of bitsequences.
         */
        for (char ch : input) {
            sequences.add(lookupTable.get(ch));
        }

        /*
         9: Assemble all bit sequences into one huge bit sequence.
         */
        BitSequence bitSequence = BitSequence.assemble(sequences);

        /*
         10: Write the huge bit sequence to the .huf file.
         */
        writer.writeObject(bitSequence);
    }
}
