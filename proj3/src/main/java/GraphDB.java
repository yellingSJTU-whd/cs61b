import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.util.Set;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

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
    private final Map<Long, Node> graph = new LinkedHashMap<>();

    static class Node {
        private final long id;
        private final double longitude;
        private final double latitude;
        private final Set<Edge> out;
        private final Set<Edge> in;

        Node(long id, double lon, double lat) {
            this.id = id;
            longitude = lon;
            latitude = lat;
            out = new HashSet<>();
            in = new HashSet<>();
        }

        public void addEdgeIn(Edge edge) {
            in.add(edge);
        }

        public void addEdgeOut(Edge edge) {
            out.add(edge);
        }

        public Set<Edge> fetchEdgesOut() {
            return out;
        }

        public Set<Edge> fetchEdgeIn() {
            return in;
        }
    }

    static class Edge {
        private final long source;
        private final long sink;
        private boolean doubleWay;
        Map<String, String> extraInfo;

        Edge(long source, long sink) {
            this.source = source;
            this.sink = sink;
            doubleWay = true;
            extraInfo = new HashMap<>();
        }

        public long fetchSource() {
            return source;
        }

        public long fetchSink() {
            return sink;
        }

        public void oneway() {
            doubleWay = false;
        }

        public void setExtraInfo(Map<String, String> infoMap) {
            extraInfo = infoMap;
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
        // TODO: Your code here.
        Collection<Node> nodes = graph.values();
        for (Node node : nodes) {
            clean(node, nodes);
        }
    }

    private void clean(Node node, Collection<Node> nodes) {
        if (node.in.isEmpty()) {
            nodes.remove(node);
            for (Edge outgoingEdge : node.out) {
                Node sink = graph.get(outgoingEdge.sink);
                sink.in.remove(outgoingEdge);
                clean(sink, nodes);
            }
        }
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
        Set<Long> adj = curr.in.stream().map(Edge::fetchSource).collect(Collectors.toSet());
        curr.out.stream().map(Edge::fetchSink).forEach(adj::add);
        return Collections.unmodifiableSet(adj);
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
            edge.setExtraInfo(infoMap);
            Node source = graph.get(edge.source);
            Node sink = graph.get(edge.sink);
            source.out.add(edge);
            sink.in.add(edge);
            System.out.println(source.id + " " + source.out);

            if (edge.doubleWay) {
                Edge reverse = new Edge(sink.id, source.id);
                reverse.setExtraInfo(infoMap);
                source.in.add(reverse);
                sink.out.add(reverse);
            }
        }
    }
}
