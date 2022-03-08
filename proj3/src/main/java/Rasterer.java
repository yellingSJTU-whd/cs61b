import java.util.HashMap;
import java.util.Map;

/**
 * This class provides all code necessary to take a query box and produce
 * a query result. The getMapRaster method must return a Map containing all
 * seven of the required fields, otherwise the front end code will probably
 * not draw the output correctly.
 */
public class Rasterer {

    public Rasterer() {
        // YOUR CODE HERE
    }

    /**
     * Takes a user query and finds the grid of images that best matches the query. These
     * images will be combined into one big image (rastered) by the front end. <br>
     * <p>
     * The grid of images must obey the following properties, where image in the
     * grid is referred to as a "tile".
     * <ul>
     *     <li>The tiles collected must cover the most longitudinal distance per pixel
     *     (LonDPP) possible, while still covering less than or equal to the amount of
     *     longitudinal distance per pixel in the query box for the user viewport size. </li>
     *     <li>Contains all tiles that intersect the query bounding box that fulfill the
     *     above condition.</li>
     *     <li>The tiles must be arranged in-order to reconstruct the full image.</li>
     * </ul>
     *
     * @param params Map of the HTTP GET request's query parameters - the query box and
     *               the user viewport width and height.
     * @return A map of results for the front end as specified: <br>
     * "render_grid"   : String[][], the files to display. <br>
     * "raster_ul_lon" : Number, the bounding upper left longitude of the rastered image. <br>
     * "raster_ul_lat" : Number, the bounding upper left latitude of the rastered image. <br>
     * "raster_lr_lon" : Number, the bounding lower right longitude of the rastered image. <br>
     * "raster_lr_lat" : Number, the bounding lower right latitude of the rastered image. <br>
     * "depth"         : Number, the depth of the nodes of the rastered image <br>
     * "query_success" : Boolean, whether the query was able to successfully complete; don't
     * forget to set this to true on success! <br>
     */
    public Map<String, Object> getMapRaster(Map<String, Double> params) {
        //construct response map
        Map<String, Object> results = new HashMap<>();
        String[][] initial = new String[1][1];
        initial[0][0] = "d0_x0_y0.png";
        results.put("render_grid", initial);
        results.put("raster_ul_lon", MapServer.ROOT_ULLON);
        results.put("raster_ul_lat", MapServer.ROOT_ULLAT);
        results.put("raster_lr_lon", MapServer.ROOT_LRLON);
        results.put("raster_lr_lat", MapServer.ROOT_LRLAT);
        results.put("depth", 0);
        results.put("query_success", false);


        //pre-processing: handle request parameters
        System.out.println(params);
        double ullat = params.get("ullat");
        double ullon = params.get("ullon");
        double lrlat = params.get("lrlat");
        double lrlon = params.get("lrlon");
        double w = params.get("w");
        double h = params.get("h");
        if (validateParams(ullat, ullon, lrlat, lrlon, w, h)) {
            return results;
        }
        results.put("query_success", true);

        //evaluate the fittest depth
        int depth = 0;
        double desired = Math.abs(ullon - lrlon) / w;
        double curr = (MapServer.ROOT_LRLON - MapServer.ROOT_ULLON) / MapServer.TILE_SIZE;
        while (curr > desired && depth < 7) {
            curr /= 2;
            depth++;
        }
        results.put("depth", depth);

        //generate render grid matrix
        double meta = (MapServer.ROOT_LRLON - MapServer.ROOT_ULLON) / Math.pow(2, depth);
        int xStart = (int) Math.ceil((ullon - MapServer.ROOT_ULLON) / meta) - 1;
        results.put("raster_ul_lon", MapServer.ROOT_ULLON + xStart * meta);
        int xEnd = (int) Math.ceil((lrlon - MapServer.ROOT_ULLON) / meta) - 1;
        results.put("raster_lr_lon", MapServer.ROOT_ULLON + (xEnd + 1) * meta);

        meta = (MapServer.ROOT_ULLAT - MapServer.ROOT_LRLAT) / Math.pow(2, depth);
        int yStart = (int) Math.ceil((MapServer.ROOT_ULLAT - ullat) / meta) - 1;
        results.put("raster_ul_lat", MapServer.ROOT_ULLAT - yStart * meta);
        int yEnd = (int) Math.ceil((MapServer.ROOT_ULLAT - lrlat) / meta) - 1;
        results.put("raster_lr_lat", MapServer.ROOT_ULLAT - (yEnd + 1) * meta);

        String[][] matrix = new String[yEnd - yStart + 1][xEnd - xStart + 1];
        for (int y = yStart; y <= yEnd; y++) {
            for (int x = xStart; x <= xEnd; x++) {
                matrix[y - yStart][x - xStart] =
                        "d" + depth + "_" + "x" + x + "_" + "y" + y + ".png";
            }
        }
        results.put("render_grid", matrix);

        return results;
    }

    private boolean validateParams(double ullat, double ullon,
                                   double lrlat, double lrlon,
                                   double w, double h) {
        return (ullon > MapServer.ROOT_LRLON) || (lrlon < MapServer.ROOT_ULLON)
                || (ullat > MapServer.ROOT_ULLAT) || (lrlat < MapServer.ROOT_LRLAT)
                || (w <= 0) || (h <= 0);
    }

}
