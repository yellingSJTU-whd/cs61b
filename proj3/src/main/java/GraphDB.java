import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Collections;
import java.util.Set;


/**
 * Graph for storing all of the intersection (vertex) and road (edge) information.
 * Uses your GraphBuildingHandler to convert the XML files into a graph. Your
 * code must include the vertices, adjacent, distance, closest, lat, and lon
 * methods. You'll also need to include instance variables and methods for
 * modifying the graph (e.g. addNode and addEdge).
 *
 * @author Alan Yao, Josh Hug
 */
public class GraphDB {
    /**
     * Your instance variables for storing the graph. You should consider
     * creating helper classes, e.g. Node, Edge, etc.
     */
    private final Map<Long, Node> graph = new HashMap<>();
    private final Map<String, List<Node>> named = new HashMap<>();
    private final Trie trie = new Trie();

    @Override
    public String toString() {
        if (graph.isEmpty()) {
            return null;
        }

        StringBuilder sb = new StringBuilder("\n");
        for (Node node : graph.values()) {
            sb.append("id = ").append(node.id).append(" ,");
            sb.append("longitude = ").append(node.longitude).append(" ,");
            sb.append("latitude = ").append(node.latitude).append("\n");
        }
        return sb.toString();
    }

    static class TrieNode {
        char c;
        boolean isWord;
        Map<Character, TrieNode> children;

        TrieNode() {
            children = new HashMap<>();
            isWord = false;
        }

        TrieNode(char c) {
            this();
            this.c = c;
        }
    }

    static class Trie {
        private final TrieNode root;
        private int num;

        Trie() {
            root = new TrieNode();
            num = 0;
        }

        public List<String> startsWith(String prefix) {
            Objects.requireNonNull(prefix);
            List<String> words = new ArrayList<>();
            if (prefix.length() < 1) {
                return words;
            }
            TrieNode curr = searchNode(prefix.toLowerCase());
            if (curr == null) {
                return words;
            }

            words = dfs(curr, new StringBuilder(prefix), words);
            return words;
        }

        private TrieNode searchNode(String key) {
            Objects.requireNonNull(key);
            TrieNode curr = root;
            int length = key.length();
            for (int i = 0; i < length; i++) {
                char ch = key.charAt(i);
                if (curr.children.containsKey(ch)) {
                    curr = curr.children.get(ch);
                } else {
                    return null;
                }
            }
            return curr;
        }

        private List<String> dfs(TrieNode currNode, StringBuilder currWord, List<String> words) {
            if (currNode.isWord) {
                words.add(currWord.toString());
            }

            for (Map.Entry<Character, TrieNode> entry : currNode.children.entrySet()) {
                char key = entry.getKey();
                TrieNode value = entry.getValue();
                words = dfs(value, new StringBuilder(currWord).append(key), words);
            }

            return words;
        }

        public void insert(String key) {
            Objects.requireNonNull(key);
            if (key.length() < 1) {
                return;
            }
            TrieNode curr = root;
            int length = key.length();
            for (int i = 0; i < length; i++) {
                char ch = key.charAt(i);
                if (curr.children.containsKey(ch)) {
                    curr = curr.children.get(ch);
                } else {
                    TrieNode node = new TrieNode(ch);
                    curr.children.put(ch, node);
                    curr = node;
                }
            }
            if (!curr.isWord) {
                curr.isWord = true;
                num++;
            }

        }

        public boolean search(String key) {
            Objects.requireNonNull(key);
            if (key.length() == 0) {
                return false;
            }
            TrieNode node = searchNode(key);
            return node != null;
        }
    }

    static class Node {
        private String name;
        private final long id;
        private final double latitude;
        private final double longitude;
        private final Map<Long, Map<String, String>> edges;

        Node(long id, double lat, double lon) {
            this.id = id;
            latitude = lat;
            longitude = lon;
            edges = new HashMap<>();
            name = null;
        }
    }

    private static String captain(String s) {
        Objects.requireNonNull(s);
        if (s.length() < 1) {
            return s;
        }
        String[] parts = s.split(" ");
        StringBuilder sb = new StringBuilder();
        for (String part : parts) {
            char[] charArr = part.toCharArray();
            if (charArr.length > 0 && charArr[0] >= 'a' && charArr[0] <= 'z') {
                charArr[0] -= 32;
            }
            sb.append(charArr).append(" ");
        }
        return sb.toString().trim();
    }

    public List<String> getLocationsByPrefix(String prefix) {
        return trie.startsWith(prefix);
    }

    public List<Map<String, Object>> getLocations(String locationName) {
        List<Node> nodes = named.get(cleanString(locationName).toLowerCase());
        List<Map<String, Object>> res = new ArrayList<>();
        if (nodes == null) {
            return res;
        }
        for (Node node : nodes) {
            Map<String, Object> infoMap = new HashMap<>(4);
            infoMap.put("lat", node.latitude);
            infoMap.put("lon", node.longitude);
            infoMap.put("name", captain(node.name));
            infoMap.put("id", node.id);
            res.add(infoMap);
        }

        return res;
    }

    void setNodeName(long id, String name) {
        Node node = graph.get(id);
        String cleanedName = cleanString(name);

        node.name = cleanedName;
        List<Node> nodes = named.getOrDefault(cleanedName, new ArrayList<>());
        nodes.add(node);
        named.put(cleanedName.toLowerCase(), nodes);

        trie.insert(name);
    }

    static class Edge {
        private final long source;
        private final long sink;
        Map<String, String> extraInfo;

        Edge(long source, long sink) {
            this.source = source;
            this.sink = sink;
            extraInfo = new HashMap<>();
        }

        @Override
        public String toString() {
            return "source= " + source + " " + "sink= " + sink;
        }
    }

    /**
     * Example constructor shows how to create and start an XML parser.
     * You do not need to modify this constructor, but you're welcome to do so.
     *
     * @param dbPath Path to the XML file to be parsed.
     */
    public GraphDB(String dbPath) {
        try {
            File inputFile = new File(dbPath);
            FileInputStream inputStream = new FileInputStream(inputFile);
            // GZIPInputStream stream = new GZIPInputStream(inputStream);

            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            GraphBuildingHandler gbh = new GraphBuildingHandler(this);
            saxParser.parse(inputStream, gbh);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        clean();
    }

    /**
     * Helper to process strings into their "cleaned" form, ignoring punctuation and capitalization.
     *
     * @param s Input string.
     * @return Cleaned string.
     */
    static String cleanString(String s) {
        return s.replaceAll("[^a-zA-Z ]", "").toLowerCase();
    }

    /**
     * Remove nodes with no connections from the graph.
     * While this does not guarantee that any two nodes in the remaining graph are connected,
     * we can reasonably assume this since typically roads are connected.
     */
    private void clean() {
        graph.values().removeIf(node -> node.edges.isEmpty());
    }

    /**
     * Returns an iterable of all vertex IDs in the graph.
     *
     * @return An iterable of id's of all vertices in the graph.
     */
    Iterable<Long> vertices() {
        return new HashSet<>(graph.keySet());
    }

    /**
     * Returns ids of all vertices adjacent to v.
     *
     * @param v The id of the vertex we are looking adjacent to.
     * @return An iterable of the ids of the neighbors of v.
     */
    Iterable<Long> adjacent(long v) {
        Node curr = graph.get(v);
        return Collections.unmodifiableSet(curr.edges.keySet());
    }

    /**
     * Returns the great-circle distance between vertices v and w in miles.
     * Assumes the lon/lat methods are implemented properly.
     * <a href="https://www.movable-type.co.uk/scripts/latlong.html">Source</a>.
     *
     * @param v The id of the first vertex.
     * @param w The id of the second vertex.
     * @return The great-circle distance between the two locations from the graph.
     */
    double distance(long v, long w) {
        return distance(lon(v), lat(v), lon(w), lat(w));
    }

    static double distance(double lonV, double latV, double lonW, double latW) {
        double phi1 = Math.toRadians(latV);
        double phi2 = Math.toRadians(latW);
        double dphi = Math.toRadians(latW - latV);
        double dlambda = Math.toRadians(lonW - lonV);

        double a = Math.sin(dphi / 2.0) * Math.sin(dphi / 2.0);
        a += Math.cos(phi1) * Math.cos(phi2) * Math.sin(dlambda / 2.0) * Math.sin(dlambda / 2.0);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return 3963 * c;
    }

    /**
     * Returns the initial bearing (angle) between vertices v and w in degrees.
     * The initial bearing is the angle that, if followed in a straight line
     * along a great-circle arc from the starting point, would take you to the
     * end point.
     * Assumes the lon/lat methods are implemented properly.
     * <a href="https://www.movable-type.co.uk/scripts/latlong.html">Source</a>.
     *
     * @param v The id of the first vertex.
     * @param w The id of the second vertex.
     * @return The initial bearing between the vertices.
     */
    double bearing(long v, long w) {
        return bearing(lon(v), lat(v), lon(w), lat(w));
    }

    static double bearing(double lonV, double latV, double lonW, double latW) {
        double phi1 = Math.toRadians(latV);
        double phi2 = Math.toRadians(latW);
        double lambda1 = Math.toRadians(lonV);
        double lambda2 = Math.toRadians(lonW);

        double y = Math.sin(lambda2 - lambda1) * Math.cos(phi2);
        double x = Math.cos(phi1) * Math.sin(phi2);
        x -= Math.sin(phi1) * Math.cos(phi2) * Math.cos(lambda2 - lambda1);
        return Math.toDegrees(Math.atan2(y, x));
    }

    /**
     * Returns the vertex closest to the given longitude and latitude.
     *
     * @param lon The target longitude.
     * @param lat The target latitude.
     * @return The id of the node in the graph closest to the target.
     */
    long closest(double lon, double lat) {
        if (graph.isEmpty()) {
            return -1;
        }

        long candidate = -1;
        double min = Long.MAX_VALUE;
        for (Map.Entry<Long, Node> entry : graph.entrySet()) {
            Node curr = entry.getValue();
            double dist = distance(curr.longitude, curr.latitude, lon, lat);
            if (dist < min) {
                candidate = entry.getKey();
                min = dist;
            }
        }

        return candidate;
    }

    /**
     * Gets the longitude of a vertex.
     *
     * @param v The id of the vertex.
     * @return The longitude of the vertex.
     */
    double lon(long v) {
        return graph.get(v).longitude;
    }

    /**
     * Gets the latitude of a vertex.
     *
     * @param v The id of the vertex.
     * @return The latitude of the vertex.
     */
    double lat(long v) {
        return graph.get(v).latitude;
    }

    void addNode(Node node) {
        graph.put(node.id, node);
    }

    void addWay(Set<Edge> edges, Map<String, String> infoMap) {
        for (Edge edge : edges) {
            edge.extraInfo = infoMap;
            Node source = graph.get(edge.source);
            Node sink = graph.get(edge.sink);
            source.edges.put(edge.sink, infoMap);
            sink.edges.put(edge.source, infoMap);
        }
    }

    String fetchWayName(long v, long w) {
        if (graph.isEmpty() || !graph.containsKey(v)) {
            return "";
        }
        Node node = graph.get(v);
        Map<String, String> map = node.edges.get(w);
        if (!map.containsKey("name")) {
//            return Router.NavigationDirection.UNKNOWN_ROAD;
            return "";
        }
        return map.get("name");
    }
}
