package com.awamo.demoussdclient.persistence.dynamo;

import org.junit.Test;

/**
 * @author 3y3r
 **/
public class DynamoClientTests {
    DynamoClient client = new DynamoClient();
    @Test
    public void testDyanamoHierarchicalClient() {
        client.createHierarchical();
    }
}
