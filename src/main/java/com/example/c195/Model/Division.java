package com.example.c195.Model;

import helper.Query;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Hashtable;

public class Division {
    public static Hashtable<String, Integer> divisionNameHash = new Hashtable<>();
    public static Hashtable<Integer, String> divisionIdHash = new Hashtable<>();

    public static ObservableList<String> countryList = FXCollections.observableArrayList();
    public static ObservableList<String> usDivisions = FXCollections.observableArrayList();
    public static ObservableList<String> ukDivisions = FXCollections.observableArrayList();
    public static ObservableList<String> canadaDivisions = FXCollections.observableArrayList();


    /** adds countries to countryList from mySQL select statement */
    public static void getCountries() throws SQLException {
        countryList.clear();
        String sql = "SELECT * FROM COUNTRIES";
        ResultSet result = Query.selectQuery(sql);
        while (result.next()) {
            String country = result.getString("Country");
            countryList.add(country);
        }
    }
    /** adds U.S divisions to usDivisions from mySQL select statement */
    public static void getUsDivisions() throws SQLException {
        usDivisions.clear();
        String sql = "SELECT * FROM first_level_divisions as d join countries as c on d.Country_ID=c.Country_ID WHERE d.Country_ID=1 ORDER BY DIVISION;";
        ResultSet result = Query.selectQuery(sql);
        while (result.next()) {
            String divName = result.getString("Division");
            usDivisions.add(divName);
        }
    }
    /** adds UK divisions to ukDivisions from mySQL select statement */
    public static void getUkDivisions() throws SQLException {
        ukDivisions.clear();
        String sql = "SELECT * FROM first_level_divisions as d join countries as c on d.Country_ID=c.Country_ID WHERE d.Country_ID=2 ORDER BY DIVISION;";
        ResultSet result = Query.selectQuery(sql);
        while (result.next()) {
            String divName = result.getString("Division");
            ukDivisions.add(divName);
        }
    }
    /** adds Canada divisions to canadaDivisions from mySQL select statement */
    public static void getCanadaDivisions() throws SQLException {
        canadaDivisions.clear();
        String sql = "SELECT * FROM first_level_divisions as d join countries as c on d.Country_ID=c.Country_ID WHERE d.Country_ID=3 ORDER BY DIVISION;";
        ResultSet result = Query.selectQuery(sql);
        while (result.next()) {
            String divName = result.getString("Division");
            canadaDivisions.add(divName);
        }
    }
    /** adds division key/value pairs to Hashtables - divisionNameHash and divisionIdHash - from the mySQL select statement*/
    public static void getDivisionHT() throws SQLException {
        divisionNameHash.clear();
        divisionIdHash.clear();
        String sql = "SELECT * FROM FIRST_LEVEL_DIVISIONS";
        ResultSet result = Query.selectQuery(sql);
        while (result.next()) {
            String divName = result.getString("Division");
            int divId = result.getInt("Division_ID");
            divisionNameHash.put(divName, divId);
            divisionIdHash.put(divId, divName);
        }
    }
}
