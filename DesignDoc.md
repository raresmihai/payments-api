# Design doc

## Working backwards

### Who is the customer?

Merchants wanting to integrate with an extensible payments API that can offer support for multiple payments processors.

### Problem summary

A payments HTTP API working prototype that:
- has a tokenizing endpoint that consumes a card number and expiry date, returning a **token**
- has a sale endpoint to process a transaction using this **token** against a payment processor's sandbox

## High-level architecture

![image](https://user-images.githubusercontent.com/16444369/137162975-e1455149-de0b-46e2-9574-bf58adfa785e.png)


The 2 endpoints are represented by 2 decoupled serveless AWS Lambda microservices.

They are fronted by API Gateway, a fully managed service that makes it easy for developers to create, publish, maintain, monitor, and secure APIs at any scale.

For storing payment data into a secured Vault, DynamoDB was used - a fully managed, serverless, key-value NoSQL database designed to run high-performance applications at any scale.

Braintree's Sandbox was used as a testing payment processor. However, the solution can be easily extended to integrate with any processor.

CloudWatch was used for logging and metrics.

## Security aspects

1. A random generated unique token is created for each pament method.
2. The API only accepts POST requests over an https endpoint.
3. No credentials hardcoded in the code. Access to different AWS services is done based on IAM role, or using credentials based on environment variables stored in Lambda (e.g. Braintree merchant id, public and private keys).
4. Different logging messages are emitted as an API is being ran, but never logging sensitive informatin (e.g. token, credit card info etc.).
5. As this is just a prototype, the APIs are currently public. This wouldn't happen in production, where we'd have token based protection. This can be achieved in different ways: generate a client token on the server side (as in Braintree's guides), configure API Gateway to enable OAuth etc.

## Implementation
Java, Maven for dependency management, Dagger2 for dependency injection, JUnit & Mockito for unit testing, Braintree gateway.

Multiple abstractions available:
- TokenizingService, CheckoutService: write different implementations for the main API business logic
- PaymentsProcessor, PaymentsProcessorResolver: allows integration with different Payments Processors
- CustomerRepository: use different data DB providers to store customer payments information (e.g. DynamoDB, RDS)
- TokenGenerator: write different algorithms for generating a token

## API
### Tokenizing endpoint
- Input: CustomerPaymentData composed of PaymentsProcessorName and CreditCardInfo (number, expiration)
- Output: TokenResponse (requestId, success, errorMessage, token)

### Checkout endpoint
- Input: TransactionRequest composed of Token and Amount
- Output: CheckoutResponse (requestId, success, errorMessage, Transaction result)

## Unit testing
Used JUnit & Mockito to test only one service as part of this prototype. But normally we should aim for code coverage > 80%.

## Development environment

SAM CLI provides a Lambda-like execution environment that lets you locally build, test, and debug applications defined by SAM templates. 
Additionally, we can keep our infrastructure as code using CDK blueprints. This will allow publishing the entire stack with a simple command.

## CI / CD

The main repo should be configured as follows:
- main branch with no permissions to push directly to it
- release branches that get 
- feature/bug/dev branches
- PullRequests with code reviews and enforcement checks (e.g. code coverage, style cops, vulnerabilities scans) in place

**Deployments**

Use of CDK pipelines to enable continuous delivery for AWS CDK applications.
Different types of stages:
- dev (each developer working on the API should have one)
- test (common test environment that contains test data)
- pre-prod (common test environments that mimics prod)
- prod

The prod stage should be regionalized and deployments to it should adhere to a follow the sun strategy to minimize impact and cause minimal disruptions.

## Metrics, Logging, Alarms
Use a CloudWatch logger to emit metrics and logs.

Additionally, there are built in metrics for each AWS service (lambda executions, errors, dynamo db writes etc.).

We can use metrics to set up alarms that alert us when something is wrong with the service (e.g. incident, email etc.).

Each alarm should come with a TSG (Troubleshooting Guide) so that the DRI (Directly Responsible Individual) can use to solve the problem withing a minimum TTM (time to mitigate).

We can use logs to troubleshoot when a problem arises.

Additionally, we can use metrics to setup analytics dashboards, follow MAU, DAU, WAU and make date driven decisions.

![image](https://user-images.githubusercontent.com/16444369/137163411-3b923487-056e-4498-9d44-4c28943a4083.png)

![image](https://user-images.githubusercontent.com/16444369/137163233-46c8902f-acf8-476f-8345-fd478c1ecac7.png)

![image](https://user-images.githubusercontent.com/16444369/137163561-45d6edc8-50e4-4ce3-9ae3-5bf69a532046.png)

![image](https://user-images.githubusercontent.com/16444369/137164268-3b13acb0-5afb-4f99-8857-a6ac80fa3e6d.png)




