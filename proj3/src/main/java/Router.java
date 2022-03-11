import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Comparator;
import java.util.Collections;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class provides a shortestPath method for finding routes between two points
 * on the map. Start by using Dijkstra's, and if your code isn't fast enough for your
 * satisfaction (or the autograder), upgrade your implementation by switching it to A*.
 * Your code will probably not be fast enough to pass the autograder unless you use A*.
 * The difference between A* and Dijkstra's is only a couple of lines of code, and boils
 * down to the priority you use to order your vertices.
 */
public class Router {
    /**
     * Return a List of longs representing the shortest path from the node
     * closest to a start location and the node closest to the destination
     * location.
     *
     * @param g       The graph to use.
     * @param stlon   The longitude of the start location.
     * @param stlat   The latitude of the start location.
     * @param destlon The longitude of the destination location.
     * @param destlat The latitude of the destination location.
     * @return A list of node id's in the order visited on the shortest path.
     */
    public static List<Long> shortestPath(GraphDB g, double stlon, double stlat,
                                          double destlon, double destlat) {
        //pre-processing
        long startID = g.closest(stlon, stlat);
        long desID = g.closest(destlon, destlat);
        if (startID == desID) {
            List<Long> path = new ArrayList<>(1);
            path.add(startID);
            return path;
        }

        Map<Long, Double> costMap = new HashMap<>();
        Map<Long, Long> preMap = new HashMap<>();
        PriorityQueue<Long> heap = new PriorityQueue<>(
                Comparator.comparingDouble(l -> costMap.get(l) + g.distance(l, desID)));
        costMap.put(startID, 0.0);
        heap.add(startID);

        long lastID = -1;
        long currID;
        while (!heap.isEmpty()) {
            currID = heap.poll();
            if (currID == desID) {
                break;
            }

            double ref = costMap.get(currID);
            for (Long adjID : g.adjacent(currID)) {
                double delta = g.distance(currID, adjID);
                if (preMap.containsKey(adjID) && costMap.get(adjID) <= ref + delta) {
                    continue;
                }
                costMap.put(adjID, ref + delta);
                preMap.put(adjID, currID);
                heap.add(adjID);
            }
            lastID = currID;
        }
        preMap.put(desID, lastID);

        //construct the shortest path: [start --> ... --> end]
        List<Long> path = new ArrayList<>();
        path.add(desID);
        long ptr = desID;
        while (preMap.get(ptr) != startID) {
            ptr = preMap.get(ptr);
            path.add(ptr);
        }
        path.add(startID);
        Collections.reverse(path);

        return path;
    }

    /**
     * Create the list of directions corresponding to a route on the graph.
     *
     * @param g     The graph to use.
     * @param route The route to translate into directions. Each element
     *              corresponds to a node from the graph in the route.
     * @return A list of NavigationDirection objects corresponding to the input
     * route.
     */
    public static List<NavigationDirection> routeDirections(GraphDB g, List<Long> route) {
        List<NavigationDirection> res = new ArrayList<>();
        if (route == null || route.size() < 2) {
            return res;
        }

        long lastNode = route.remove(0);
        String lastWay = null;
        double miles = 0.0;
        double lastBearing = Double.NEGATIVE_INFINITY;
        int dir = 0;
        NavigationDirection navi = new NavigationDirection();

        while (!route.isEmpty()) {
            long currNode = route.remove(0);
            String currWay = g.fetchName(lastNode, currNode);
            double mile = g.distance(lastNode, currNode);
            double currBearing = g.bearing(lastNode, currNode);

            if (lastWay != null && !lastWay.equals(currWay)) {
                navi.direction = dir;
                navi.way = lastWay;
                navi.distance = miles;
                miles = 0;
                res.add(navi);
                navi = new NavigationDirection();
                dir = NavigationDirection.evalDir(currBearing, lastBearing);
            }

            lastNode = currNode;
            lastBearing = currBearing;
            lastWay = currWay;
            miles += mile;
        }
        navi.direction = dir;
        navi.way = lastWay;
        navi.distance = miles;
        res.add(navi);

        return res; // FIXME
    }


    /**
     * Class to represent a navigation direction, which consists of 3 attributes:
     * a direction to go, a way, and the distance to travel for.
     */
    public static class NavigationDirection {

        /**
         * Integer constants representing directions.
         */
        public static final int START = 0;
        public static final int STRAIGHT = 1;
        public static final int SLIGHT_LEFT = 2;
        public static final int SLIGHT_RIGHT = 3;
        public static final int RIGHT = 4;
        public static final int LEFT = 5;
        public static final int SHARP_LEFT = 6;
        public static final int SHARP_RIGHT = 7;

        /**
         * Number of directions supported.
         */
        public static final int NUM_DIRECTIONS = 8;

        /**
         * A mapping of integer values to directions.
         */
        public static final String[] DIRECTIONS = new String[NUM_DIRECTIONS];

        /**
         * Default name for an unknown way.
         */
        public static final String UNKNOWN_ROAD = "unknown road";

        /** Static initializer. */
        static {
            DIRECTIONS[START] = "Start";
            DIRECTIONS[STRAIGHT] = "Go straight";
            DIRECTIONS[SLIGHT_LEFT] = "Slight left";
            DIRECTIONS[SLIGHT_RIGHT] = "Slight right";
            DIRECTIONS[LEFT] = "Turn left";
            DIRECTIONS[RIGHT] = "Turn right";
            DIRECTIONS[SHARP_LEFT] = "Sharp left";
            DIRECTIONS[SHARP_RIGHT] = "Sharp right";
        }

        /**
         * The direction a given NavigationDirection represents.
         */
        int direction;
        /**
         * The name of the way I represent.
         */
        String way;
        /**
         * The distance along this way I represent.
         */
        double distance;

        /**
         * Create a default, anonymous NavigationDirection.
         */
        public NavigationDirection() {
            this.direction = STRAIGHT;
            this.way = UNKNOWN_ROAD;
            this.distance = 0.0;
        }

        static int evalDir(double currBearing, double lastBearing) {
            if (lastBearing == Double.NEGATIVE_INFINITY) {
                return START;
            }

            double delta = currBearing - lastBearing;
            if (delta > 180) {
                delta -= 360;
            }
            if (delta < -180) {
                delta += 360;
            }

            boolean positive = delta > 0.0;
            double absDelta = Math.abs(delta);
            if (absDelta <= 15) {
                return STRAIGHT;
            } else if (absDelta <= 30) {
                return positive ? SLIGHT_RIGHT : SLIGHT_LEFT;
            } else if (absDelta <= 100) {
                return positive ? RIGHT : LEFT;
            } else {
                return positive ? SHARP_RIGHT : SHARP_LEFT;
            }
        }

        public String toString() {
            return String.format("%s on %s and continue for %.3f miles.",
                    DIRECTIONS[direction], way, distance);
        }

        /**
         * Takes the string representation of a navigation direction and converts it into
         * a Navigation Direction object.
         *
         * @param dirAsString The string representation of the NavigationDirection.
         * @return A NavigationDirection object representing the input string.
         */
        public static NavigationDirection fromString(String dirAsString) {
            String regex = "([a-zA-Z\\s]+) on ([\\w\\s]*) and continue for ([0-9\\.]+) miles\\.";
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(dirAsString);
            NavigationDirection nd = new NavigationDirection();
            if (m.matches()) {
                String direction = m.group(1);
                switch (direction) {
                    case "Start":
                        nd.direction = NavigationDirection.START;
                        break;
                    case "Go straight":
                        nd.direction = NavigationDirection.STRAIGHT;
                        break;
                    case "Slight left":
                        nd.direction = NavigationDirection.SLIGHT_LEFT;
                        break;
                    case "Slight right":
                        nd.direction = NavigationDirection.SLIGHT_RIGHT;
                        break;
                    case "Turn right":
                        nd.direction = NavigationDirection.RIGHT;
                        break;
                    case "Turn left":
                        nd.direction = NavigationDirection.LEFT;
                        break;
                    case "Sharp left":
                        nd.direction = NavigationDirection.SHARP_LEFT;
                        break;
                    case "Sharp right":
                        nd.direction = NavigationDirection.SHARP_RIGHT;
                        break;
                    default:
                        return null;
                }

                nd.way = m.group(2);
                try {
                    nd.distance = Double.parseDouble(m.group(3));
                } catch (NumberFormatException e) {
                    return null;
                }
                return nd;
            } else {
                // not a valid nd
                return null;
            }
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof NavigationDirection) {
                return direction == ((NavigationDirection) o).direction
                        && way.equals(((NavigationDirection) o).way)
                        && distance == ((NavigationDirection) o).distance;
            }
            return false;
        }

        @Override
        public int hashCode() {
            return Objects.hash(direction, way, distance);
        }
    }
}
