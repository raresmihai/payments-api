The endpoints can be invoked at https://3q2ygvaex2.execute-api.us-east-1.amazonaws.com.

Only a test stage has been deployed, meaning that the full access URIs are:
- https://3q2ygvaex2.execute-api.us-east-1.amazonaws.com/test/tokenize
- https://3q2ygvaex2.execute-api.us-east-1.amazonaws.com/test/checkout

**Example POST CURLs**

## Register a payment method 

Requires:
- CreditCardInfo (card number and expiration date)
- PaymentsProcessorType: Braintree is currently the only value supported

```
curl -X POST \

  'https://3q2ygvaex2.execute-api.us-east-1.amazonaws.com/test/tokenize' \
  
  -H 'content-type: application/json' \
  
  -d '{ "paymentsProcessorType": "Braintree", "creditCardInfo": { "number": "4009348888881881", "expirationDate": "05/29" }}'
```
  
Testing credit card numbers: https://developer.paypal.com/braintree/docs/reference/general/testing

## Process a transaction

Requires:
- Valid Payment method token (Returned calling the API above)
- Amount (number)

```
curl -X POST \                                  
  'https://3q2ygvaex2.execute-api.us-east-1.amazonaws.com/test/checkout' \
  -H 'content-type: application/json' \
  -d '{ "token": "<payment-method-token>", "amount": 10}'
```  
