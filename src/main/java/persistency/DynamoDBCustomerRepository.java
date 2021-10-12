package persistency;

import com.amazonaws.services.dynamodbv2.document.*;
import com.amazonaws.services.dynamodbv2.document.spec.PutItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import model.input.CreditCardInfo;
import model.input.CustomerPaymentData;
import model.input.PaymentsProcessorName;
import persistency.exception.RepositoryException;
import persistency.exception.TokenDoesntExistException;

import java.util.HashMap;
import java.util.Map;

public class DynamoDBCustomerRepository implements CustomerRepository {
    private final DynamoDB dynamoDb;

    private final String tableName;

    private final String indexName;

    public DynamoDBCustomerRepository(DynamoDB dynamoDB, String tableName, String indexName) {
        this.dynamoDb = dynamoDB;
        this.tableName = tableName;
        this.indexName = indexName;
    }

    @Override
    public boolean customerPaymentAlreadyExists(CustomerPaymentData customerPaymentData) {
        Index index = this.dynamoDb.getTable(this.tableName).getIndex(this.indexName);
        QuerySpec spec = new QuerySpec()
                .withKeyConditionExpression("creditCardNumber = :v_number")
                .withFilterExpression("paymentProcessor = :v_processor")
                .withValueMap(new ValueMap()
                        .withString(":v_number", customerPaymentData.getCreditCardInfo().getNumber())
                        .withString(":v_processor", customerPaymentData.getPaymentsProcessorType().toString()));
        ItemCollection<QueryOutcome> items = index.query(spec);
        return items.firstPage() != null && items.firstPage().size() > 0;
    }

    @Override
    public void save(String token, CustomerPaymentData customerPaymentData) {
        this.dynamoDb.getTable(tableName).putItem(
                    new PutItemSpec().withItem(new Item()
                            // TODO: save a CustomerItem object directly and with nested attributes (e.g. CreditCardInfo) instead of putting each attribute separately
                            .withPrimaryKey("token", token)
                            .withString("paymentProcessor", customerPaymentData.getPaymentsProcessorType().toString())
                            .withString("creditCardNumber", customerPaymentData.getCreditCardInfo().getNumber())
                            .withString("creditCardExpirationDate", customerPaymentData.getCreditCardInfo().getExpirationDate())));
    }

    @Override
    public CustomerPaymentData getCustomerPaymentDataByToken(String token) throws RepositoryException {
        Map<String, String> expressionAttributeNames = new HashMap<String, String>();
        expressionAttributeNames.put("#payment_token", "token");
        QuerySpec spec = new QuerySpec()
                .withKeyConditionExpression("#payment_token = :v_token")
                .withValueMap(new ValueMap()
                    .withString(":v_token", token))
                .withNameMap(expressionAttributeNames);

        ItemCollection<QueryOutcome> items = this.dynamoDb.getTable(this.tableName).query(spec);
        if (items.firstPage() == null || items.firstPage().size() == 0) {
            throw new TokenDoesntExistException("The provided token doesn't exist in the Vault.");
        }

        Map<String, Object> attributeValues = items.firstPage().iterator().next().asMap();
        return new CustomerPaymentData(
                PaymentsProcessorName.valueOf((String)attributeValues.get("paymentProcessor")),
                new CreditCardInfo((String)attributeValues.get("creditCardNumber"), (String)attributeValues.get("creditCardExpirationDate")));
    }
}
