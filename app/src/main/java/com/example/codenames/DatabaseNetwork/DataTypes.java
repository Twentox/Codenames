package com.example.codenames.DatabaseNetwork;

public enum DataTypes {
    HEADER("<-type:header->"),
    REQUEST("<-type:request->"),
    COMMAND("<-type:command->"),
    ANWSER("<-type:anwser->");

    private String type;

    DataTypes(String type){
        this.type = type;
    }
    public String getType() {
        return type;
    }
}
