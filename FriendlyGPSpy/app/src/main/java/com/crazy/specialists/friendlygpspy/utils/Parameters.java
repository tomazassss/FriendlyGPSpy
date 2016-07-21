package com.crazy.specialists.friendlygpspy.utils;

/**
 * Created by Nesta on 7/21/2016.
 */
public class Parameters {

    // The minimum distance to change Updates in meters
    public static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters

    // The minimum time between updates in milliseconds
    public static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute

    public static final String PAIRED_IP_PROPERTY = "pairedIp";
    public static final String DEFAULT_IP = "localhost";


    //TODO: Should be configurable
    public static final int SERVER_PORT = 46761;
}
