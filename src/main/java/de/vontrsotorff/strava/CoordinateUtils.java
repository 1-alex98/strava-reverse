package de.vontrsotorff.strava;

import de.vontrsotorff.strava.domain.Coordinate;

public class CoordinateUtils {
    public static final double R = 6.3781E6;


    public static double distance(Coordinate first, Coordinate second) {
        double theta = first.lon() - second.lon();
        double dist = Math.sin(deg2rad(first.lat())) * Math.sin(deg2rad(second.lat())) + Math.cos(deg2rad(first.lat())) * Math.cos(deg2rad(second.lat())) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        dist = dist * 1.609344 * 1000;
        return (dist);
    }

    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    /*::  This function converts decimal degrees to radians             :*/
    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    /*::  This function converts radians to decimal degrees             :*/
    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    private static double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    private static double toX(double lon, double pitch1){
        return R * deg2rad(lon) * Math.cos(deg2rad(pitch1));
    }

    private static double toY(double lat){
        return R * deg2rad(lat);
    }

    private static double fromXToLon(double x, double pitch1){
        return rad2deg(x /(R*x));
    }

    private static double fromYToLat(double y){
        return rad2deg(y / R);
    }

    public static double shortestDistance(Coordinate lineOne, Coordinate lineTwo, Coordinate three){
        var lineOneX= toX(lineOne.lon(), lineOne.lat());
        var lineTwoX= toX(lineTwo.lon(), lineTwo.lat());
        var threeX= toX(three.lon(), three.lat());
        var lineOneY= toY(lineOne.lat());
        var lineTwoY= toY(lineTwo.lat());
        var threeY= toY(three.lat());

        return shortestDistance(lineOneX, lineOneY, lineTwoX, lineTwoY, threeX, threeY);
    }


    private static double shortestDistance(double x1, double y1, double x2, double y2, double x3, double y3)
    {
        var px=x2-x1;
        var py=y2-y1;
        var temp=(px*px)+(py*py);
        var u=((x3 - x1) * px + (y3 - y1) * py) / (temp);
        if(u>1){
            u=1;
        }
        else if(u<0){
            u=0;
        }
        var x = x1 + u * px;
        var y = y1 + u * py;

        var dx = x - x3;
        var dy = y - y3;
        return Math.sqrt(dx*dx + dy*dy);
    }
}
