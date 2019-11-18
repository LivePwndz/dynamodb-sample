package com.awamo.demoussdclient.persistence.dynamo;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBSaveExpression;
import com.amazonaws.services.dynamodbv2.document.*;
import com.amazonaws.services.dynamodbv2.model.*;
import com.awamo.demoussdclient.persistence.dynamo.entity.DSubscriber;
import com.awamo.demoussdclient.persistence.dynamo.entity.DSubscriberTenant;
import com.awamo.demoussdclient.persistence.dynamo.entity.DTenant;

import java.util.*;

/**
 * @author 3y3r
 **/
public class DynamoClient {

    public static final String USSD_SUBSCRIPTION_TABLE_NAME = "ussd_subscription_v5";
    public static final String USSD_SUBSCRIPTION_HIERARCHICAL_TABLE_NAME = "ussd_subscription_schema_v6";

    public static final String MSISDN_INDEX_NAME = "msisdnIndex";
    public static final String TENANT_INDEX_NAME = "tenantIndex";
    public static final String TABLE_PARTITION_KEY_NAME = "client_id";
    public static final String TABLE_SORT_KEY_NAME = "tenant_id";
    public static final String MSISDN_INDEX_SORT_KEY_NAME = "msisdn";
    public static final String SUBSCRIBER_PIN_KEY_NAME = "pin";
    public static final String USSD_USERNAME_KEY_NAME = "ussd_username";
    public static final String USSD_PASSWORD_KEY_NAME = "ussd_password";
    public static final String TENANT_TABLE_PARTITION_KEY_PREFIX = "tenant_id_cred_";


    private AmazonDynamoDB getDbClient() {
        AwsClientBuilder.EndpointConfiguration endpointConfig = new AwsClientBuilder.EndpointConfiguration("http://localhost:8000", Regions.EU_CENTRAL_1.getName());
        AmazonDynamoDBClientBuilder builder = AmazonDynamoDBClientBuilder.standard();
        builder.setEndpointConfiguration(endpointConfig);
        builder.setCredentials(getCredsProvider());
        AmazonDynamoDB ddbClient = builder.build();
        return ddbClient;
    }

    private AWSCredentialsProvider getCredsProvider() {
        AWSCredentialsProvider credsProvider = new AWSCredentialsProvider() {
            @Override
            public AWSCredentials getCredentials() {
                AWSCredentials creds = new AWSCredentials() {
                    @Override
                    public String getAWSAccessKeyId() {
                        return "sample";
                    }

                    @Override
                    public String getAWSSecretKey() {
                        return "sample";
                    }
                };
                return creds;
            }

            @Override
            public void refresh() {

            }
        };

        return credsProvider;
    }


    private boolean doesUssdSubscriptionHierarchicalTableExist(AmazonDynamoDB ddbClient) {
        try {
            ddbClient.describeTable(USSD_SUBSCRIPTION_HIERARCHICAL_TABLE_NAME);
        } catch (ResourceNotFoundException ex) {
            //TODO Log this error.
            return false;
        }

        return true;

    }



    public DynamoDBMapper getDbMapper(){
        //DynamoDBMapperConfig config = new DynamoDBMapperConfig.Builder().wi.withTableNameOverride(DynamoDBMapperConfig.TableNameOverride.withTableNameReplacement(USSD_SUBSCRIPTION_TABLE_NAME)).build();
        return new DynamoDBMapper(getDbClient(), getCredsProvider());
    }


    private CreateTableRequest ussdSubscriptionHierarchicalCreateTableRequest() {
        String tablePartitionKeyName = TABLE_PARTITION_KEY_NAME; // Can be any name. msisdn for now.
        String tableSortKeyName = TABLE_SORT_KEY_NAME; // Can be any name. tenant name for now.
        String msisdnIndexSortKeyName = MSISDN_INDEX_SORT_KEY_NAME;

        KeySchemaElement tablePartitionKeySchema = new KeySchemaElement(TABLE_PARTITION_KEY_NAME, KeyType.HASH);
        KeySchemaElement tableSortKeySchema = new KeySchemaElement(TABLE_SORT_KEY_NAME, KeyType.RANGE);

        KeySchemaElement msisdnIndexPartitionKeySchema = new KeySchemaElement(TABLE_SORT_KEY_NAME, KeyType.HASH);
        KeySchemaElement msisdnIndexSortKeySchema = new KeySchemaElement(MSISDN_INDEX_SORT_KEY_NAME, KeyType.RANGE);

        KeySchemaElement tenantIndexPartitionKeySchema = new KeySchemaElement(MSISDN_INDEX_SORT_KEY_NAME, KeyType.HASH);
        KeySchemaElement tenantIndexSortKeySchema = new KeySchemaElement(TABLE_SORT_KEY_NAME, KeyType.RANGE);

        List<KeySchemaElement> tableKeySchemaElements = new ArrayList<>();
        tableKeySchemaElements.add(tablePartitionKeySchema);
        tableKeySchemaElements.add(tableSortKeySchema);

        List<KeySchemaElement> msisdnIndexKeySchemaElements = new ArrayList<>();
        msisdnIndexKeySchemaElements.add(msisdnIndexPartitionKeySchema);
        msisdnIndexKeySchemaElements.add(msisdnIndexSortKeySchema);

        List<KeySchemaElement> tenantIndexKeySchemaElements = new ArrayList<>();
        tenantIndexKeySchemaElements.add(tenantIndexPartitionKeySchema);
        tenantIndexKeySchemaElements.add(tenantIndexSortKeySchema);

        GlobalSecondaryIndex msisdnIndex = new GlobalSecondaryIndex()
                .withIndexName(MSISDN_INDEX_NAME)
                .withProvisionedThroughput(new ProvisionedThroughput()
                        .withReadCapacityUnits((long) 10)
                        .withWriteCapacityUnits((long) 1))
                .withProjection(new Projection().withProjectionType(ProjectionType.INCLUDE).withNonKeyAttributes(SUBSCRIBER_PIN_KEY_NAME, TABLE_PARTITION_KEY_NAME));
        msisdnIndex.withKeySchema(msisdnIndexKeySchemaElements);


        GlobalSecondaryIndex tenantIndex = new GlobalSecondaryIndex()
                .withIndexName(TENANT_INDEX_NAME)
                .withProvisionedThroughput(new ProvisionedThroughput()
                        .withReadCapacityUnits((long) 10) // TODO To finetune this and all other occurances.
                        .withWriteCapacityUnits((long) 1)) // TODO To finetune this.
                .withProjection(new Projection().withProjectionType(ProjectionType.INCLUDE).withNonKeyAttributes(SUBSCRIBER_PIN_KEY_NAME));
        tenantIndex.withKeySchema(tenantIndexKeySchemaElements);




        AttributeDefinition tablePartitionKey = new AttributeDefinition(TABLE_PARTITION_KEY_NAME, ScalarAttributeType.S);
        AttributeDefinition tableSortKey = new AttributeDefinition(TABLE_SORT_KEY_NAME, ScalarAttributeType.S);
        AttributeDefinition msisdnIndexSortKey = new AttributeDefinition(MSISDN_INDEX_SORT_KEY_NAME, ScalarAttributeType.S);

        List<AttributeDefinition> attributeDefinitions = new ArrayList<>();
        attributeDefinitions.add(tablePartitionKey);
        attributeDefinitions.add(tableSortKey);
        attributeDefinitions.add(msisdnIndexSortKey);


        CreateTableRequest request = new CreateTableRequest()
                .withAttributeDefinitions(attributeDefinitions)
                .withKeySchema(tableKeySchemaElements)
                .withProvisionedThroughput(new ProvisionedThroughput(
                        new Long(10), new Long(10)))
                .withTableName(USSD_SUBSCRIPTION_HIERARCHICAL_TABLE_NAME)
                .withGlobalSecondaryIndexes(msisdnIndex, tenantIndex);

        return request;
    }


    public void createHierarchical() {
        AmazonDynamoDB ddbClient = getDbClient();
        if (!doesUssdSubscriptionHierarchicalTableExist(ddbClient)) {
            CreateTableResult createTableResults = ddbClient.createTable(ussdSubscriptionHierarchicalCreateTableRequest());
            ddbClient.listTables(1);
            String tableStatus = createTableResults.getTableDescription().getTableStatus();
            System.out.format("Table status: %s \n", tableStatus);
        }

        saveTenant();
        saveSubscriber();
        updateSubscriber();
        getTenantCreds();
        putItem2();
        findTenantsByMsisdn();
        ddbClient.shutdown();

    }

    private void updateSubscriber() {
        DSubscriber subscriber = new DSubscriber();
        Map expected = new HashMap();
        expected.put(TABLE_PARTITION_KEY_NAME, new ExpectedAttributeValue(new AttributeValue().withS("2")).withExists(true));
        expected.put(TABLE_SORT_KEY_NAME, new ExpectedAttributeValue(new AttributeValue().withS("2")).withExists(true));
        expected.put(MSISDN_INDEX_SORT_KEY_NAME, new ExpectedAttributeValue(new AttributeValue().withS("0781")).withExists(true));

        DynamoDBSaveExpression saveExp = new DynamoDBSaveExpression()
                .withConditionalOperator(ConditionalOperator.AND);
        saveExp.setExpected(expected);

        subscriber.setClientId("2");
        subscriber.setTenantId("2");
        subscriber.setMsisdn("0781");
        subscriber.setPin("1234");
        getDbMapper().save(subscriber, saveExp);
    }

    private void saveSubscriber() {
        DSubscriber subscriber = new DSubscriber();
        subscriber.setClientId("2");
        subscriber.setTenantId("2");
        subscriber.setMsisdn("0781");
        getDbMapper().save(subscriber);
    }

    private void saveTenant() {
        DTenant subscriber = new DTenant();
        Map expected = new HashMap();

        String tenantId = "1";
        String tenantCredId = TENANT_TABLE_PARTITION_KEY_PREFIX+tenantId;

        subscriber.setTenantCredId(tenantCredId);
        subscriber.setTenantId(tenantId);
        subscriber.setUsername("sample ussd user");
        subscriber.setPassword("tenant_"+tenantId+"_pass");
        getDbMapper().save(subscriber);
    }

    private void findTenantsByMsisdn2() {
        String clientId = "1";
        Map<String, String> expressionAttributesNames = new HashMap<>();
        expressionAttributesNames.put("#client_id", TABLE_PARTITION_KEY_NAME);

        Map<String, AttributeValue> expressionAttributeValues = new HashMap<>();
        expressionAttributeValues.put(":client_id", new AttributeValue().withN(clientId));


        DynamoDBQueryExpression<DSubscriberTenant> queryExpression = new DynamoDBQueryExpression<DSubscriberTenant>()
                .withKeyConditionExpression("#client_id = :client_id")
                .withExpressionAttributeNames(expressionAttributesNames)
                .withExpressionAttributeValues(expressionAttributeValues).withConsistentRead(true);

        List<DSubscriberTenant> tenants = getDbMapper().query(DSubscriberTenant.class, queryExpression);
        System.out.format("Printing tenants raw data: "+clientId+"\n");
        tenants.stream().forEach(System.out::println);

    }


    private void findTenantsByMsisdn() {

        String msisdn = "0781";
        Map<String, String> expressionAttributesNames = new HashMap<>();
        expressionAttributesNames.put("#msisdn", MSISDN_INDEX_SORT_KEY_NAME);

        Map<String, AttributeValue> expressionAttributeValues = new HashMap<>();
        expressionAttributeValues.put(":msisdn", new AttributeValue().withS(msisdn));


        DynamoDBQueryExpression<DSubscriberTenant> queryExpression = new DynamoDBQueryExpression<DSubscriberTenant>()
                .withKeyConditionExpression("#msisdn = :msisdn")
                .withExpressionAttributeNames(expressionAttributesNames)
                .withExpressionAttributeValues(expressionAttributeValues).withConsistentRead(false)
                .withIndexName(TENANT_INDEX_NAME);

        List<DSubscriberTenant> tenants = getDbMapper().query(DSubscriberTenant.class, queryExpression);
        System.out.format("Printing tenants for msisdn: "+msisdn+"\n");
        tenants.stream().forEach(System.out::println);

    }

    private void getTenantCreds() {

        String tenantId = "1";
        String tenantCredId = TENANT_TABLE_PARTITION_KEY_PREFIX+tenantId;
        Map<String, String> expressionAttributesNames = new HashMap<>();

        String expressionPartitionAttrName = "#"+TABLE_PARTITION_KEY_NAME;
        String expressionSortAttrName = "#"+TABLE_SORT_KEY_NAME;

        String expressionPartitionAttrValue = ":"+TABLE_PARTITION_KEY_NAME;
        String expressionSortAttrValue = ":"+TABLE_SORT_KEY_NAME;


        expressionAttributesNames.put(expressionPartitionAttrName, TABLE_PARTITION_KEY_NAME);
        expressionAttributesNames.put(expressionSortAttrName, TABLE_SORT_KEY_NAME);

        Map<String, AttributeValue> expressionAttributeValues = new HashMap<>();
        expressionAttributeValues.put(expressionPartitionAttrValue, new AttributeValue().withS(tenantCredId));
        expressionAttributeValues.put(expressionSortAttrValue, new AttributeValue().withS(tenantId));


        DynamoDBQueryExpression<DTenant> queryExpression = new DynamoDBQueryExpression<DTenant>()
                .withKeyConditionExpression(expressionPartitionAttrName+" = "+expressionPartitionAttrValue+" and "+expressionSortAttrName+" = "+expressionSortAttrValue)
                .withExpressionAttributeNames(expressionAttributesNames)
                .withExpressionAttributeValues(expressionAttributeValues).withConsistentRead(true);

        List<DTenant> tenants = getDbMapper().query(DTenant.class, queryExpression);
        System.out.format("Printing tenants creds for tenant id: "+tenantId+" and tenant cred id:"+tenantCredId+"\n");
        tenants.stream().forEach(System.out::println);

    }

    private void putItem2 (){
        DynamoDB db = new DynamoDB(getDbClient());
        Table table = db.getTable(USSD_SUBSCRIPTION_HIERARCHICAL_TABLE_NAME);
        Item item = new Item();
        item.withPrimaryKey(TABLE_PARTITION_KEY_NAME, "2",TABLE_SORT_KEY_NAME, "1");
        item.withString(MSISDN_INDEX_SORT_KEY_NAME,"0781");
        item.withString(SUBSCRIBER_PIN_KEY_NAME,"1234");
        table.putItem(item);
    }

}
