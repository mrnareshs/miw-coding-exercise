**Summary**

 **Describe and explain your surge pricing design.**

  It is a simple SpringBoot application with a standard flow of Controller -> Service -> Repositories -> Model.
Class SurgePriceProcessor contains the bulk of logic of computing Surge pricing of an item. SecurityConfig Class takes care of authentication and authorization.
GlobalExceptionHandler takes care of handling different exceptions in the application and handling it gracefully.

**Describe your API endpoints design and model. Include one example of a request and response for each API call.**

There are 2 Entities involved in this project i.e. Item and Order

### **Item:**

```
Http Method - Get
API Name: findAllItems()
Request: localhost:8088/items
Response:
[
{
"id": 1,
"name": "39",
"description": "n.s@k.com",
"price": 2.1,
"quantity": 2
},
{
"id": 2,
"name": "Pizza",
"description": "n.s@k.com",
"price": 5,
"quantity": 11
}
]__
```
```
Http Method - Get 
API Name: findItemById(Long itemId)
Request: localhost:8088/items/2
Response: 
    {
    "id": 2,
    "name": "Pizza",
    "description": "Pizza Description",
    "price": 5.5,
    "quantity": 11
    }
   ```

```
Http Method - Post 
API Name: saveItem(Item item)
Authentication: Admin/12345
Request: localhost:8088/items
Body:
{
    "name": "Cookies",
    "description": "Cookies Description",
    "price": 2,
    "quantity": 200
}
Response: 
    {
    "id": 13,
    "name": "Cookies",
    "description": "Cookies Description",
    "price": 2,
    "quantity": 200
}
   ```
### **Order:**

```
Http Method - Get
API Name: listAllOrders()
Request: localhost:8088/orders
Response:
[
    {
        "id": 6,
        "itemId": 1,
        "orderBy": "user",
        "pricePerUnit": 2.1,
        "orderQty": 2
    },
    {
        "id": 7,
        "itemId": 1,
        "orderBy": "user",
        "pricePerUnit": 2.1,
        "orderQty": 12
    }
]
```
```
Http Method - Get 
API Name: findOrderById(Long itemId)
Request: localhost:8088/orders/8
Response: 
    {
    "id": 8,
    "itemId": 1,
    "orderBy": "user",
    "pricePerUnit": 2.1,
    "orderQty": 4
}
   ```

```
Http Method - Post 
API Name: addOrder(OrderDTO order)
Basic Authentication: user/12345
Request: localhost:8088/orders
Body:
{
    "itemId": "2",
    "pricePerUnit": 5,
    "orderQty": 1
}
Response: 
{
    "id": 12,
    "itemId": 2,
    "orderBy": "user",
    "pricePerUnit": 5,
    "orderQty": 1
}
   ```
**Explain the authentication mechanism you chose, and why.**

I have used InMemoryAuthentication because its simple and quick to develop. In a production environment, I would have considered something complex and much secure OAuth based authentication.

**Explain your testing, what you covered, and why.**

I have created JUnits for my Service classes and also one Integration Test for Item Entity. I could have added more however its a time cruch.

**Describe the features you would include if this was an actual business application.** 
Its a very open-ended application and can contains a lot of features that are standards in any E-Commerce application like Promotions, Wishlist for Customers, etc.
