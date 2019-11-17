package com.awamo.demoussdclient.persistence;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;

/**
 * @author 3y3r
 **/
public class NoSQLClient {

    private NoSQLClient(){}
    public static MongoDatabase call(){
        MongoClientURI uri = new MongoClientURI(
                "mongodb+srv://karma:karma@maidenc-o7hxl.mongodb.net/test?retryWrites=true&w=majority");

        MongoClient mongoClient = new MongoClient(uri);
        MongoDatabase database = mongoClient.getDatabase("test");
        return database;
    }
}
