package com.awamo.demoussdclient.persistence;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Arrays;

import static java.util.Collections.singletonList;

/**
 * @author 3y3r
 **/
public class NoSQLClientTests {
    private static MongoDatabase database;
    @BeforeClass
    public static void setup() {
        database = NoSQLClient.call();
    }

    @Test
    public void context() {
        Assert.assertNotNull(database);
        //database.createCollection("codex-v1");
        MongoCollection collection = database.getCollection("codex-v1");
        Document canvas = new Document("item", "canvas")
                .append("qty", 100)
                .append("tags", singletonList("cotton"));

        Document size = new Document("h", 28)
                .append("w", 35.5)
                .append("uom", "cm");
        canvas.put("size", size);

        collection.insertOne(canvas);
        long docCount = collection.countDocuments();
        System.out.println("Doc count: " + docCount);
        FindIterable<Document> docs = collection.find();
        int i = 0;
        if (docs.iterator().hasNext()) {
            i++;
            Document doc = docs.iterator().next();
            doc.put("item", "cotton_"+i);
            System.out.println("Doc: " + doc.getString("item"));
        }
    }
}
