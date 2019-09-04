package com.aws2.demo;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;
import sun.dc.pr.PRError;

import javax.swing.table.DefaultTableCellRenderer;
import java.util.HashMap;
import java.util.Map;

public class DynamoDBDEMO {
    private static final Region region = Region.CA_CENTRAL_1;
    private static final String DEFAULT_TABLE_NAME = "test";
    private static DynamoDbClient dynamoDbClient = DynamoDbClient.builder().region(region).build();
    public static void main(String[] args) {
        DynamoDBDEMO.getItem();
    }

    private static void createTable(){
        CreateTableRequest createTableRequest = CreateTableRequest.builder()
                .attributeDefinitions(AttributeDefinition.builder()
                        .attributeName("Name")
                        .attributeType(ScalarAttributeType.S)
                        .build())
                .keySchema(KeySchemaElement.builder()
                        .attributeName("Name")
                        .keyType(KeyType.HASH)
                        .build())
                .provisionedThroughput(ProvisionedThroughput.builder()
                        .readCapacityUnits(new Long(10))
                        .writeCapacityUnits(new Long(10))
                        .build())
                .tableName("test")
                .build();

        CreateTableResponse createTableResponse = dynamoDbClient.createTable(createTableRequest);
        System.out.println(createTableResponse.tableDescription().tableName());
    }

    private static void listTable(){
        ListTablesResponse listTablesResponse = dynamoDbClient.listTables();
        listTablesResponse.tableNames().stream().forEach(x -> System.out.println(x));
    }

    private static void putItem(){
        HashMap<String, AttributeValue> hashMap = new HashMap<>();
        hashMap.put("Name", AttributeValue.builder().s("item").build());
        PutItemRequest putItemRequest = PutItemRequest.builder()
                .tableName(DEFAULT_TABLE_NAME)
                .item(hashMap)
                .build();
        dynamoDbClient.putItem(putItemRequest);
    }

    private static void getItem(){
        HashMap<String, AttributeValue> hashMap = new HashMap<>();
        hashMap.put("Name", AttributeValue.builder().s("item").build());
        GetItemRequest getItemRequest = GetItemRequest.builder()
                .key(hashMap)
                .tableName(DEFAULT_TABLE_NAME)
                .build();
        Map<String, AttributeValue> response = dynamoDbClient.getItem(getItemRequest).item();
        for (Map.Entry entry: response.entrySet()){
            System.out.println(entry.getKey() +":" + response.get(entry.getKey()).toString());
        }
    }
}
