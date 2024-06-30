package com.example.codenames.DatabaseNetwork;

public class NetworkEngine {
    public static final String ARTEFACTID = "DatenBruecke";
    public static final String VERSION = "1.0-Snapshot";

    public static boolean checkVersion(String artefactid, String version){
        return artefactid.contains(ARTEFACTID) && version.contains(version);
    }

}
