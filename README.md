## Tools / Software dependencies
You need to have following installed on your machine
  * JDK 1.8
  * Scala 2.12.8
  * sbt

## How to Run
Navigate to root folder on terminal

run build.sh file to setup the docker image

run following command to start the application on port number 9000
```sbt run 9000```


Goto browser and enter [URL http://localhost:9000](http://localhost:9000)

## Available Routes
```
GET      /
GET      /api/v1/sensors/{uuid}/metrics
POST     /api/v1/sensors/{uuid}/mesurements
GET      /api/v1/sensors/{uuid}
GET      /api/v1/sensors/{uuid}/alerts
```

## To run tests, run
``` sbt test ```

### Planned Enhancements
  * At present the `InMemoryStorage` class is a place holder for database, I am planning to replace it with Some database e.g `MySql` or some in memory database like `H2` or  `Apache Jena`
  * For Handling heavy requests I have planned to use Akka actors (the present solution is with synchronous blocks which could slow down the process, akka is a good solution to the synchronization problem).