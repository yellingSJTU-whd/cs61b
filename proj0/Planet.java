public class Planet {
    public double xxPos;
    public double yyPos;
    public double xxVel;
    public double yyVel;
    public double mass;
    public String imgFileName;
    private static final double G = 6.67e-11;

    public Planet(double xP, double yP, double xV, double yV, double m, String img) {
        xxPos = xP;
        yyPos = yP;
        xxVel = xV;
        yyVel = yV;
        mass = m;
        imgFileName = img;
    }

    public Planet(Planet p) {
        this(p.xxPos, p.yyPos, p.xxVel, p.yyVel, p.mass, p.imgFileName);
    }

    public double calcDistance(Planet p) {
        double xDist = this.xxPos - p.xxPos;
        double yDist = this.yyPos - p.yyPos;
        return Math.sqrt(xDist * xDist + yDist * yDist);
    }

    public double calcForceExertedBy(Planet p) {
        double distance = calcDistance(p);
        return G * this.mass * p.mass / (distance * distance);
    }

    public double calcForceExertedByX(Planet p) {
        if (xxPos == p.xxPos) {
            return 0;
        }
        double dist = calcDistance(p);
        double force = calcForceExertedBy(p);
        return (p.xxPos - xxPos) / dist * force;
    }

    public double calcForceExertedByY(Planet p) {
        if (yyPos == p.yyPos) {
            return 0;
        }
        double dist = calcDistance(p);
        double force = calcForceExertedBy(p);
        return (p.yyPos - yyPos) / dist * force;
    }

    public double calcNetForceExertedByX(Planet[] planets) {
        if (null == planets || planets.length == 0) {
            return 0;
        }
        double netXForce = 0;
        for (Planet p : planets) {
            if (!this.equals(p)) {
                netXForce += calcForceExertedByX(p);
            }
        }
        return netXForce;
    }

    public double calcNetForceExertedByY(Planet[] planets) {
        if (null == planets || planets.length == 0) {
            return 0;
        }
        double netYForce = 0;
        for (Planet p : planets) {
            if (!this.equals(p)) {
                netYForce += calcForceExertedByY(p);
            }
        }
        return netYForce;
    }

    public void update(double dt, double fX, double fY) {
        double aX = fX / mass;
        double aY = fY / mass;
        xxVel += aX * dt;
        yyVel += aX * dt;
        xxPos += xxVel * dt;
        yyPos += yyVel * dt;
    }

    public void draw() {
        StdDraw.picture(xxPos, yyPos, "images/" + imgFileName);
    }
}
