package com.awamo.demoussdclient.persistence.dynamo.entity;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.awamo.demoussdclient.persistence.dynamo.DynamoClient;

/**
 * @author 3y3r
 **/

@DynamoDBTable(tableName= DynamoClient.USSD_SUBSCRIPTION_HIERARCHICAL_TABLE_NAME)
public class DTenant {
    private String tenantCredId;
    private String tenantId;
    private String username;
    private String password;


    @DynamoDBHashKey( attributeName = DynamoClient.TABLE_PARTITION_KEY_NAME)
    public String getTenantCredId() {
        return tenantCredId;
    }
    public void setTenantCredId(String tenantCredId) {
        this.tenantCredId = tenantCredId;
    }

    @DynamoDBRangeKey(attributeName=DynamoClient.TABLE_SORT_KEY_NAME)
    public String getTenantId() {
        return tenantId;
    }
    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    @DynamoDBAttribute(attributeName= DynamoClient.USSD_USERNAME_KEY_NAME)
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @DynamoDBAttribute(attributeName= DynamoClient.USSD_PASSWORD_KEY_NAME)
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }



    @Override
    public String toString() {
        return "DSubscriber{" +
                "clientId='" + tenantCredId + '\'' +
                ", tenantId='" + tenantId + '\'' +
                ", username='" + username + '\'' +
                ", password='************'"+'\''+
                '}';
    }
}
