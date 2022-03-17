import org.junit.Before;
import org.junit.Test;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.List;
import java.util.Collections;
import java.util.Arrays;
import java.util.HashMap;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class TestRouter {
    private static final String PARAMS_FILE = "path_params.txt";
    private static final String RESULTS_FILE = "path_results.txt";
    private static final int NUM_TESTS = 8;
    private static final String OSM_DB_PATH = "../library-sp18/data/berkeley-2018.osm.xml";
    private static GraphDB graph;
    private static boolean initialized = false;

    @Before
    public void setUp() throws Exception {
        if (initialized) {
            return;
        }
        graph = new GraphDB(OSM_DB_PATH);
        initialized = true;
    }

    @Test
    public void testShortestPath() throws Exception {
        List<Map<String, Double>> testParams = paramsFromFile();
        List<List<Long>> expectedResults = resultsFromFile();

        for (int i = 0; i < NUM_TESTS; i++) {
            System.out.println(String.format("Running test: %d", i));
            Map<String, Double> params = testParams.get(i);
            List<Long> actual = Router.shortestPath(graph,
                    params.get("start_lon"), params.get("start_lat"),
                    params.get("end_lon"), params.get("end_lat"));
            List<Long> expected = expectedResults.get(i);
            Collections.reverse(expected);
            Collections.reverse(actual);
            String errMsg = "\nexpected = " + expected + "\nactual = " + actual;
            assertEquals(errMsg, expected, actual);
        }
    }

    @Test
    public void testGetLocationsByPrefix() {
        String prefix = "de";
        String[] arr = {"De Afghanan Kabob House", "De Visu", "De La Salle Hall",
            "Derby Food Center", "Designer Cuts", "Deliverance Temple Church",
            "Dentist Bryan D. Haynes", "Dentistry"};
        List<String> expected = Arrays.asList(arr);
        List<String> actual = graph.getLocationsByPrefix(prefix);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetLocationsByPrefixWithSpace() {
        String prefix = "los ";
        String[] arr = {"Los Canteros taqueria", "Los Cilantros",
            "Los Conteros Taqueria", "Los Moles"};
        List<String> expected = Arrays.asList(arr);
        List<String> actual = graph.getLocationsByPrefix(prefix);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetLocations() {
        String location = "stuffed inn";
        Map<String, Object> infoMap = new HashMap<>();
        infoMap.put("name", "Stuffed Inn");
        infoMap.put("lon", -122.2601117);
        infoMap.put("id", 318886225L);
        infoMap.put("lat", 37.8756956);
        List<Map<String, Object>> expect = new ArrayList<>();
        expect.add(infoMap);

        List<Map<String, Object>> actual = graph.getLocations(location);
        assertEquals(expect.size(), actual.size());
        assertEquals(expect, actual);
    }

    @Test
    public void testGetLocationsWithSpace() {
        String location = "style ";
        Map<String, Object> infoMap = new HashMap<>();
        infoMap.put("name", "Style 2000");
        infoMap.put("lon", -122.2885637);
        infoMap.put("id", 2370938232L);
        infoMap.put("lat", 37.8905967);
        List<Map<String, Object>> expect = new ArrayList<>();
        expect.add(infoMap);

        List<Map<String, Object>> actual = graph.getLocations(location);
        assertEquals(expect.size(), actual.size());
        assertEquals(expect, actual);
    }

    private List<Map<String, Double>> paramsFromFile() throws Exception {
        List<String> lines = Files.readAllLines(Paths.get(PARAMS_FILE), Charset.defaultCharset());
        List<Map<String, Double>> testParams = new ArrayList<>();
        int lineIdx = 2; // ignore comment lines
        for (int i = 0; i < NUM_TESTS; i++) {
            Map<String, Double> params = new HashMap<>();
            params.put("start_lon", Double.parseDouble(lines.get(lineIdx)));
            params.put("start_lat", Double.parseDouble(lines.get(lineIdx + 1)));
            params.put("end_lon", Double.parseDouble(lines.get(lineIdx + 2)));
            params.put("end_lat", Double.parseDouble(lines.get(lineIdx + 3)));
            testParams.add(params);
            lineIdx += 4;
        }
        return testParams;
    }

    private List<List<Long>> resultsFromFile() throws Exception {
        List<String> lines = Files.readAllLines(Paths.get(RESULTS_FILE), Charset.defaultCharset());
        List<List<Long>> expected = new ArrayList<>();
        int lineIdx = 2; // ignore comment lines
        for (int i = 0; i < NUM_TESTS; i++) {
            int numVertices = Integer.parseInt(lines.get(lineIdx));
            lineIdx++;
            List<Long> path = new ArrayList<>();
            for (int j = 0; j < numVertices; j++) {
                path.add(Long.parseLong(lines.get(lineIdx)));
                lineIdx++;
            }
            expected.add(path);
        }
        return expected;
    }
}
