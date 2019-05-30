## Tools / Software dependencies
You need to have following installed on your machine
  * JDK 1.8
  * Scala 2.12.8
  * sbt

## How to Run
Navigate to root folder on terminal
run following command to start the application on port number 9000
```sbt run 9000```


Goto browser and enter [URL http://localhost:9000](http://localhost:9000)

## Available Routes
```
GET      /
GET      /api/v1/sensors/$uuid<[0-9,A-Z,-]+>/metrics
POST     /api/v1/sensors/$uuid<[0-9,A-Z,-]+>/mesurements
GET      /api/v1/sensors/$uuid<[0-9,A-Z,-]+>
GET      /api/v1/sensors/$uuid<[0-9,A-Z,-]+>/alerts
```
