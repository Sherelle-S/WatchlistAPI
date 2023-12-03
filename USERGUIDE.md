# **Watchlist API User Guide**

welcome to the watchlist API, this user guide will help you to understand how to use the watchlist API.

# **Table of Contents**

- [**Watchlist API User Guide**](#watchlist-api-user-guide)
- [**Table of Contents**](#table-of-contents)
  - [**Introduction**](#introduction)
  - [**Getting Started**](#getting-started)
  - [**Best Practices**](#best-practices)
    - [**Troubleshooting**](#troubleshooting)
  - [**Demo**](#demo)

## **Introduction**

The Watchlist API is designed to manage a user's watchlist of stocks. This user guide serves as a comprehensive reference to interact with the various functionalities provided by the API.

## **Getting Started**

To use the Currency Converter application, follow these steps:

1. Clone the repository to your local machine.

   ```bash
   git clone <repository-url>

2. Open the Currency COnverter project in your preferred Java IDE

3. If unsure how to access the API end points sign up for a postman free Postman account. The application will create a file to host your watchlist in. 

4. W


## **Usage**
### **Controllers**
The API offers the following controllers:

## **Usage**

### **Controllers**

The API offers the following controllers:

| Endpoint                 | HTTP Method | Description                               | Parameters       | Request Body                   | Response                                |
|--------------------------|-------------|-------------------------------------------|------------------|--------------------------------|-----------------------------------------|
| `/watchlist/`            | GET         | Retrieve all watchlist data               | -                | -                              | Returns a list of watchlist entries.    |
| `/watchlist/sortedWatchlist` | GET     | Retrieve watchlist data sorted by stock name | -              | -                              | Returns a sorted list of watchlist entries. |
| `/watchlist/searchName/{name}` | GET     | Search for watchlist entries by stock name | {name} - Stock name to search | -                        | Returns matching watchlist entries.    |
| `/watchlist/addEntry`    | POST        | Create a new watchlist entry              | -                | JSON array of watchlist entries | Returns a success status upon creation. |
| `/watchlist/updateEntry/{uuid}` | PUT    | Update a watchlist entry by UUID          | {uuid} - Unique identifier of the entry | JSON object with updated entry details | Returns a success status upon update. |
| `/watchlist/deleteEntry/{uuid}` | DELETE | Remove a watchlist entry by UUID          | {uuid} - Unique identifier of the entry | -                              | Returns a success status upon deletion. |



### ***API Endpoints***

The Watchlist API is structured around several endpoints, each serving specific functionalities:

- **/watchlist/**
  - **GET:** Retrieves all watchlist data.
    - **Response:** Returns a list of watchlist entries.
    
- **/watchlist/sortedWatchlist**
  - **GET:** Retrieves watchlist data sorted by stock name.
    - **Sorting Mechanism:** Utilizes QuickSort for efficient sorting by stock name.
    - **Response:** Returns a sorted list of watchlist entries.
    
- **/watchlist/searchName/{name}**
  - **GET:** Searches for watchlist entries by stock name.
    - **Search Method:** Employs Binary Search for quick and efficient name-based queries.
    - **Parameters:** {name} - Stock name to search.
    - **Response:** Returns matching watchlist entries.
    
- **/watchlist/addEntry**
  - **POST:** Creates a new watchlist entry.
    - **Request:** Accepts a JSON array of watchlist entries.
    - **Response:** Returns a success status upon creation.
    
- **/watchlist/updateEntry/{uuid}**
  - **PUT:** Updates a watchlist entry by UUID.
    - **Parameters:** {uuid} - Unique identifier of the entry.
    - **Request:** Accepts a JSON object with updated entry details.
    - **Response:** Returns a success status upon update.
    
- **/watchlist/deleteEntry/{uuid}**
  - **DELETE:** Removes a watchlist entry by UUID.
    - **Parameters:** {uuid} - Unique identifier of the entry.
    - **Response:** Returns a success status upon deletion.

The API leverages efficient algorithms such as QuickSort for sorting the watchlist by stock name and Binary Search for quickly retrieving watchlist entries based on stock names. These algorithms ensure optimal performance and quick response times, even with large datasets, providing users with a seamless experience.



### ***Values accepted by the API***

The values and formats accepted in this watchlist API in a POST request are:

- **UUID uuid** - Automatically assigned and does not need to be inputted by the user.
- **String stockName** - Name of the stock that you are wanting to watch.
- **String symbol** - Symbol of the stock that you are wanting to watch.
- **String currency** - Currency that you purchased the stock in.
- **LocalDate datePurchased** - A string in the date pattern "dd/MM/yyyy".
- **Integer wantsVolStock** - How much stock you would like to acquire.
- **Integer ownsVolStock** - The amount of stocks that you currently own.
- **double purchasePrice** - The price that you purchased the stock at.
- **double currentPrice** - Its current price.
- **double profit** - This field is not populated by the user; it is dynamically calculated based on the data given by the user.
- **double pointsChange** - This field is not calculated by the user; it is dynamically calculated by an algorithm that takes the opening price and closing price to calculate how the points have changed from the time the market opened to the time it closed.
- **double open** - The price of the stock when the market opened.
- **double close** - The price of the stock when the market closed.
- **double intradayHigh** - The intraday high.

please use the following format to Post a watchlist item
```[
    {
    "stockName": "SAMPLE STOCK",
    "symbol": "SMPL",
    "currency": "USD",
    "datePurchased": "16/11/2023",
    "ownsVolStock": 750,
    "wantsVolStock": 1500,
    "purchasePrice": 1.29,
    "currentPrice": 1.52,
    "open": 1.20,
    "close": 1.52,
    "intradayHigh": 1.59
    }
]
```

Upon updating the API, the following fields are able to be changed in a PUT request:
- **String symbol**
- **String currency**
- **Integer OwnsVolStock**
- **Integer WantsVolStock**
- **double currentPrice**
- **double purchasePrice**
- **double open**
- **double close**
- **double intraday high**

update stock as follows

```{
    "stockName": "SAMPLE STOCK",
    "symbol": "SMPL",
    "currency": "USD",
    "datePurchased": "16/11/2023",
    "ownsVolStock": 1000,
    "wantsVolStock": 1500,
    "purchasePrice": 1.29,
    "currentPrice": 2.82,
    "open": 1.20,
    "close": 1.52,
    "intradayHigh": 2.89
    }
```
## **Best Practices**

- Use appropriate HTTP methods for each operation (GET, POST, PUT, DELETE).
- Ensure authentication tokens or credentials are kept secure while making requests.

### **Troubleshooting**

Users might encounter several common issues:

- **Incorrect Endpoint Usage:** Ensure the use of correct endpoint URLs for desired operations.
- **Invalid Input:** Validate if the provided data adheres to the required format and structure.
- **Authentication Errors:** Verify the validity and proper inclusion of access credentials in requests.


## **Demo**

![GIF](https://res.cloudinary.com/dft0cktie/image/upload/v1700422856/bandicam_2023-11-19_17-35-44-177_w9rumv.gif)


