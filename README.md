## Table of contents
* [Why Rentpal?](#why-rentpal)
* [Features](#features)
* [REST API](#rest-api)
* [Tools and Technologies](#tools-and-technologies)

## Why Rentpal?
When I came to Paris, I got an accommodation and the owner gave me 4 sheets of rental agreement. I was with 2 other roommates and they also got the same. The question we asked to ourselves was the point of getting a rental agreement that is bound to expire within 1 year. During the year of 2018-2019, there were around 358,000 international students who came to study in france, according to campus france. Now lets do a little math here, let us consider 200,000 students took an accommodation and they received a rental agreement for 3 pages. So in total 600,000 papers required. Moreover, the lease agreement could for 6 months or 1 year. After that there is no usage of these 600,000 papers. Now this calculation is just for international students, think about the local students who move to different cities, foriegners who come to france for work purpose, the tally could be little bit higher. Also, let us think about the other countries who take international students and now the tally becomes much more. 

Now can we do this in a better way? I think we can and use the concept of electronic signature. A owner and tenant can create an contract by signing electronically. The electronic copy will be saved in a server. Whenever the owner or tenant needs to access agreement, they can view document from UI interface. The tenant can share the agreement via email and can revoke access anytime.

## Features
- Property Management - A owner can manage properties and the information in these properties helps us to build an rental agreement.
- Tenant Management - A owner can add a tenant to a property and share rental agreement to the tenant.

## REST API

| API  | Method | Content type | Parameter | Response Status | Response | 
| ------------- | ------------- | ------------- | ------------- | ------------- | ------------- |
| /properties  | GET  | N/A | searchQuery="maison" | <p>Success - 200 <br> Errors - 400</p> |[{propertyobject},{propertyobject}]|
| /properties  | POST  | application/json | {propertyobject} | <p>Success - 200 <br> Errors - 400, 422</p> |{propertyobject}|
| /properties/{id} | PUT | application/json | {propertyobject} | <p>Success - 200 <br> Errors - 400, 422</p> |{propertyobject}|
| /properties/{id}  | DELETE  | N/A | N/A | <p>Success - 204 <br> Errors - 400</p> |N/A|
|/properties/{id}/units| GET | N/A | N/A | <p>Success - 200 <br> Errors - 400</p> |[{unitobject},{unitobject}]|
|/properties/{id}/units| POST | application/json | {unitobject}| <p>Success - 200 <br> Errors - 400, 422</p> |{unitobject}|
|/properties/{id}/units/{unitid}| PUT | application/json | {unitobject} | <p>Success - 200 <br> Errors - 400, 422</p> |{unitobject}|
| /properties/{id}/units/{unitid}  | DELETE  | N/A | N/A | <p>Success - 204 <br> Errors - 400</p> |N/A|

1) 200 - Successful 
2) 204 - Successful with no body
3) 400 - Bad request, in case if invalid value in request url
4) 422 - Invalid parameters

Property Object<br/>
```{"propertyName":"Maison", "addressLine1":"10B", "addressLine2":"Place Des Martrys", "city":"Choisy", "postal":"94400"}```

Unit Object<br/>
```{"area":12.11, "bathrooms":1, "bedrooms":5, "cautionDeposit": 450, "doorNumber":"10A", "floorNumber":2, "furnished":true, "rent": 400}```

## Tools and Technologies
- Spring boot - To build restful service.
- Spring eureka - Agreement service registers with eureka for service discovery.
- Language - Java 11
- Unit testing - JUnit 5
- Database - PostgreSQL(FTS using GIN)
