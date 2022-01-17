import java.util.ArrayList;
import java.util.List;

public class NBody {

    public static Planet[] readPlanets(String planetsTxtPath) {
        In in = new In(planetsTxtPath);
        List<Planet> planets = new ArrayList<>(5);
        in.readInt();
        in.readDouble();
        while (in.hasNextLine()) {
            double xP = Double.parseDouble(in.readString());
            double yP = Double.parseDouble(in.readString());
            double xV = Double.parseDouble(in.readString());
            double yV = Double.parseDouble(in.readString());
            double m = Double.parseDouble(in.readString());
            String img = in.readString();
            planets.add(new Planet(xP, yP, xV, yV, m, img));
        }
        return planets.toArray(new Planet[5]);
    }


    public static double readRadius(String planetsTxtPath) {
        In in = new In(planetsTxtPath);
        in.readInt();
        return in.readDouble();
    }

    public static void main(String[] args) {
        //drawing the initial universe state
        double T = Double.parseDouble(args[0]);
        double dt = Double.parseDouble(args[1]);
        String filename = args[2];
        double radius = readRadius(filename);
        Planet[] planets = readPlanets(filename);
        int n = planets.length;

        StdDraw.setScale(-radius, radius);
        StdDraw.picture(0, 0, "images/starfield.jpg");

        for (Planet planet : planets) {
            planet.draw();
        }

        //creating an animation
        StdDraw.enableDoubleBuffering();

        for (double simulationTime = 0; simulationTime < T; simulationTime += dt) {
            double[] xForces = new double[n];
            double[] yForces = new double[n];
            for (int i = 0; i < n; i++) {
                xForces[i] = planets[i].calcNetForceExertedByX(planets);
                yForces[i] = planets[i].calcNetForceExertedByY(planets);
            }
            for (int i = 0; i < n; i++) {
                planets[i].update(dt, xForces[i], yForces[i]);
            }
            StdDraw.picture(0, 0, "images/starfield.jpg");
            for (Planet planet : planets) {
                planet.draw();
            }
            StdDraw.show();
            StdDraw.pause(10);
        }

        //printing the universe
        StdOut.printf("%d\n", planets.length);
        StdOut.printf("%.2e\n", radius);
        for (Planet planet : planets) {
            StdOut.printf("%11.4e %11.4e %11.4e %11.4e %11.4e %12s\n",
                    planet.xxPos, planet.yyPos, planet.xxVel, planet.yyVel, planet.mass, planet.imgFileName);
        }
    }
}
