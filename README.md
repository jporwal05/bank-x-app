# bank-x-app
This is a sample application for the Bank X API.

## Building the application
You need to have jdk-17 on your machine.
- Clone the repository
- Run `./mvnw clean install` to build the application

## Running the application
- Run `./mvnw spring-boot:run` to run the application

## API
The application has the postman collection and environment files in the `resources/postman` folder. Import the files in Postman.
It has the following endpoints:
1. `POST /bankx/api/auth/register` - Register a new user
2. `POST /bankx/api/auth/authenticate` - Authenticate a user
3. `POST /bankx/api/accounts/{accountId}/{accountType}/deposit` - Deposit money into an account
4. `POST /bankx/api/accounts/{accountId}/{accountType}/withdraw` - Withdraw money from an account
5. `POST /bankx/api/accounts/{fromAccountId}/transfer/{toAccountId}` - Transfer money from one account to another
6. `GET /bankx/api/transactions/accountId/{accountId}` - Get all transactions for an account
7. `GET /bankx/api/transactions/category/{category}` - Get all transactions for a category
8. `POST /bankx/api/transactions/process` - Process transactions

## Testing via Postman
Important: The environment file should also be imported in Postman.
Run the following APIs in the given order:
1. `POST /bankx/api/auth/register` - Register a new user
    - This also sets the token as en environment variable which would be used for further API calls.
    - Note the id(s) of the account(s) created. This would be used in the next steps.
2. `POST /bankx/api/accounts/{accountId}/{accountType}/deposit` - Deposit money into an account
    - Use this to deposit `200` units of money into current account as the balance is zero now 
3. `POST /bankx/api/accounts/{fromAccountId}/transfer/{toAccountId}` - Transfer money from one account to another
    - Use this to transfer `100` units of money from current account to savings account
4. `GET /bankx/api/transactions/accountId/{accountId}` - Get all transactions for an account
    - Use this to get all transactions for the current account
5. `POST /bankx/api/transactions/process` - Process transactions
    - Use this to process all transactions for the current account. The transaction reference should be put as `From Bank Z` and the category should be put as `INTER_BANK`.
6. `GET /bankx/api/transactions/category/{category}` - Get all transactions for a category
    - Use this to get all transactions for the `INTER_BANK` category

The above sequence tests all the requirements stated in the requirement document.

## Important notes
- The application uses an in-memory database. So, the data is lost when the application is stopped. The data is also not persisted across multiple runs of the application.
- The application only tests happy paths. It does not test for negative scenarios.
- There are no validations as of now.
- However, the API is simple enough to satisfy the given requirements.
- JUnits are also not present as of now.
- Notifications are just logged, not implemented.