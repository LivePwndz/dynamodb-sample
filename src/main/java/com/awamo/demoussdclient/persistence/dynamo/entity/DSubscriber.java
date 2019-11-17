package com.awamo.demoussdclient.persistence.dynamo.entity;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.awamo.demoussdclient.persistence.dynamo.DynamoClient;

/**
 * @author 3y3r
 **/

@DynamoDBTable(tableName= DynamoClient.USSD_SUBSCRIPTION_HIERARCHICAL_TABLE_NAME)
public class DSubscriber {
    private String clientId;
    private String tenantId;
    private String msisdn;
    private String pin;


    @DynamoDBHashKey( attributeName = DynamoClient.TABLE_PARTITION_KEY_NAME)
    public String getClientId() {
        return clientId;
    }
    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    @DynamoDBRangeKey(attributeName=DynamoClient.TABLE_SORT_KEY_NAME)
    public String getTenantId() {
        return tenantId;
    }
    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }


    @DynamoDBAttribute(attributeName= DynamoClient.MSISDN_INDEX_SORT_KEY_NAME)
    public String getMsisdn() {
        return msisdn;
    }
    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    @DynamoDBAttribute(attributeName= DynamoClient.SUBSCRIBER_PIN_KEY_NAME)
    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    @Override
    public String toString() {
        return "DSubscriber{" +
                "clientId='" + clientId + '\'' +
                ", tenantId='" + tenantId + '\'' +
                ", msisdn='" + msisdn + '\'' +
                ", pin='**********'"+'\''+
                '}';
    }
}
