package com.example.codenames.services.postgresql;
public abstract class SQLStatements {
    public static final String GET_PUBLIC_TABLES = "SELECT table_name FROM information_schema.tables WHERE table_schema = 'public'";
    public static String selectAllFromTable(String table){
        return "SELECT * from '"+table+"';";
    }
    public static String getColumnsFormTable(String table){
        return "SELECT column_name FROM information_schema.columns WHERE table_schema = 'public' AND table_name = '"+table+"';";
    }
    public static String getColumnFromTable(String column, String table){
        return "SELECT "+column+" FROM "+table+";";
    }
    public static String getValuesFromColumnAndTable(String column, String table, int von, int bis){
        return "SELECT "+column+" FROM "+table+" LIMIT "+String.valueOf(bis-von)+" OFFSET "+String.valueOf(von-1)+";";
    }
}
