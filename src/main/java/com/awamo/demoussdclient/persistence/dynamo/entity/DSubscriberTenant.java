package com.awamo.demoussdclient.persistence.dynamo.entity;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.awamo.demoussdclient.persistence.dynamo.DynamoClient;

/**
 * @author 3y3r
 **/

@DynamoDBTable(tableName = DynamoClient.USSD_SUBSCRIPTION_HIERARCHICAL_TABLE_NAME)
public class DSubscriberTenant {
    private String clientId;
    private String msisdn;
    private String tenantId;



    @DynamoDBHashKey(attributeName = DynamoClient.TABLE_PARTITION_KEY_NAME)
    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    @DynamoDBIndexHashKey(globalSecondaryIndexName = DynamoClient.TENANT_INDEX_NAME, attributeName = DynamoClient.MSISDN_INDEX_SORT_KEY_NAME)
    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }


    @DynamoDBIndexRangeKey(globalSecondaryIndexName = DynamoClient.TENANT_INDEX_NAME, attributeName = DynamoClient.TABLE_SORT_KEY_NAME)
    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }


    @Override
    public String toString() {
        return "Tenant {" +
                "clientId='" + clientId + '\'' +
                ", tenantId='" + tenantId + '\'' +
                ", msisdn='" + msisdn + '\'' +
                '}';
    }
}
