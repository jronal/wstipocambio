*Reto BCP by @jronal*

### depedendencias
```
spring-boot: 2.6.0
spring-boot-starter-data-jpa
spring-boot-starter-web
com.h2database
lombok
jsonwebtoken
bouncycastle
rxjava2
```


### build
```
mvn clean install
```

### Run desde comando
```
mvnw.cmd spring-boot:run
```



### build and run  image docker

```
docker build -t wstipocambio
docker run -it -p 8089:8089 wstipocambio
docker images
```

### generar token jwt con el metodo GET "/api/v1/tipocambio/jwt/{DNI}"
```
curl --location --request GET 'http://localhost:8089/api/v1/tipocambio/jwt/12345678'
```

### metodo calcular tipo cambio

```
curl --location --request GET 'http://localhost:8089/api/v1/tipocambio/100/USD/PEN' \
--header 'Authorization: Bearer eyJhbGciOiJFUzI1NiJ9.eyJpc3MiOiJGUFMiLCJzdWIiOiI0MTY5MTYwMCIsImF1ZCI6ImF1ZCIsImV4cCI6MTYzNzU0MDU5MywibmJmIjoxNjM3NTMzMzkzLCJpYXQiOjE2Mzc1MzMzOTN9.xmAt12IVlBrfMrZ1iTQUgl01HC0DxYIgMywV6WitYhAsaY8zCV3Hff1x-oq87G11lyXVc8FJKD8Af6kH5mFnWQ'
```


### metodo actualizar tipo cambio
```
curl --location --request POST 'http://localhost:8089/api/v1/tipocambio/' \
--header 'Authorization: Bearer eyJhbGciOiJFUzI1NiJ9.eyJpc3MiOiJGUFMiLCJzdWIiOiI0MTY5MTYwMCIsImF1ZCI6ImF1ZCIsImV4cCI6MTYzNzU0MDU5MywibmJmIjoxNjM3NTMzMzkzLCJpYXQiOjE2Mzc1MzMzOTN9.xmAt12IVlBrfMrZ1iTQUgl01HC0DxYIgMywV6WitYhAsaY8zCV3Hff1x-oq87G11lyXVc8FJKD8Af6kH5mFnWQ' \
--header 'Content-Type: application/json' \
--data-raw '{
"monedaOrigen":"PEN",
"monedaDestino":"USD",
"cotizacion":5.000
}'
```