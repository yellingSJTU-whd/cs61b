public class Planet {
    double xxPos;
    double yyPos;
    double xxVel;
    double yyVel;
    double mass;
    String imgFileName;
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
        double xDist;
        double xForce = G * p.mass * mass;
        boolean sign = p.xxPos > xxPos;
        if (sign) {
            xDist = p.xxPos - xxPos;
            xForce /= (xDist * xDist);
        } else {
            xDist = xxPos - p.xxPos;
            xForce = -xForce / (xDist * xDist);
        }
        return xForce;
    }

    public double calcForceExertedByY(Planet p) {
        if (yyPos == p.yyPos) {
            return 0;
        }
        double yDist;
        double yForce = G * p.mass * mass;
        boolean sign = p.yyPos > yyPos;
        if (sign) {
            yDist = p.yyPos - yyPos;
            yForce /= (yDist * yDist);
        } else {
            yDist = yyPos - p.yyPos;
            yForce = -yForce / (yDist * yDist);
        }
        return yForce;
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
        StdDraw.picture(xxPos, yyPos, imgFileName);
    }

    @Override
    public boolean equals(Object obj) {
        if (null == obj) {
            return false;
        }
        if (!(obj instanceof Planet p)) {
            return false;
        }
        return xxPos == p.xxPos && yyPos == p.yyPos && xxVel == p.xxVel && yyVel == p.yyPos && mass == p.mass && imgFileName.equals(p.imgFileName);
    }
}
