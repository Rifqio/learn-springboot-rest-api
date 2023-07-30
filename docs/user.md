# User API Spec

## Register User

- Endpoint: POST /api/auth/register

Request Body:

```json
{
  "username" : "<String>",
  "password" : "<String>",
  "name" : "<String>"
}
```

Response Body (Success) : 
```json
{
  "data" : "<String>"
}
```

Response Body (Error) :
```json
{
  "data" : "error",
  "errors" : "<String>"
}
```
## Login User
- Endpoint: POST /api/auth/login

Request Body:

```json
{
  "username" : "<String>",
  "password" : "<String>"
}
```

Response Body (Success) :
```json
{
  "data" : {
    "token" : "<String>",
    "expiredAt" : "<Integer>"
  }
}
```

Response Body (Error) :
```json
{
  "errors" : "<String>"
}
```
## Get User

Endpoint : GET /api/users/current

Request Header :

- X-API-TOKEN : Token (Mandatory)

Response Body (Success) :

```json
{
  "data" : {
    "username" : "<String>",
    "name" : "<String>"
  }
}
```

Response Body (Failed, 401) :

```json
{
  "errors" : "<String>"
}
```

## Update User

Endpoint : PATCH /api/users/current

Request Header :

- X-API-TOKEN : Token (Mandatory)

Request Body :

```json
{
  "name" : "<String>", // put if only want to update name
  "password" : "<String>" // put if only want to update password
}
```

Response Body (Success) :

```json
{
  "data" : {
    "username" : "<String>",
    "name" : "<String>"
  }
}
```

Response Body (Failed, 401) :

```json
{
  "errors" : "<String>"
}
```

## Logout User

Endpoint : DELETE /api/auth/logout

Request Header :

- X-API-TOKEN : Token (Mandatory)

Response Body (Success) :

```json
{
  "data" : "<String>"
}
```
