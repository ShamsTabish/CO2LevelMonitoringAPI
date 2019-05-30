##Introduction
Carbon Dioxide (CO2) is all around us and we are constantly expelling it, but in high concentration CO2 can be harmful or even lethal.

CO2 Levels between 2000 and 5000 ppm are associated with headaches, sleepiness poor concentration, loss of attention, increased heart rate and slight nausea may also be present.

##Assignment
Your assignment is to build a service capable of collecting data from hundreds of thousands of sensors and alert if the CO2 concentrations reach critical levels.

##Acceptance criteria
• The service should be able to receive measurements from each sensor at the rate of 1 per minute
• If the CO2 level exceeds 2000 ppm the sensor status should be set WARN
• If the service receives 3 or more consecutive measurements higher than 2000 the sensor status should be set to ALERT
• When the sensor reaches to status ALERT an alert should be stored
• If the service receives 3 or more consecutive measurements lower than 2000 the sensor status should be set to OK
• The service should provide the following metrics about each sensor
    – Average CO2 level for the last 30 days
    – Maximum CO2 Level in the last 30 days
• It is possible to list all the alerts for a given sensor

##API
####Collect sensor mesurements

POST /api/v1/sensors/{uuid}/mesurements
{
    "co2" : 2000,
    "time" : "2019-02-01T18:55:47+00:00"
}

####Sensor status
GET /api/v1/sensors/{uuid}
Response:
{
    "status" : "OK" // Possible status OK,WARN,ALERT
}

####Sensor metrics
GET /api/v1/sensors/{uuid}/metrics
Response:
{
    "maxLast30Days" : 1200,
    "avgLast30Days" : 900
}

####Listing alerts
GET /api/v1/sensors/{uuid}/alerts
Response:
[
    {
            "startTime" : "2019-02-02T18:55:47+00:00",
            "endTime" : "2019-02-02T20:00:47+00:00",
            "mesurement1" : 2100,
            "mesurement2" : 2200,
            "mesurement3" : 2100,
    }
]

##Deliverable
The final deliverable is a link to a project on github that meets the following criteria:
    • The service implements the previously documented API
    • Meets the previously defined acceptance criteria
    • Is easy to read and maintain
    • Can be easily built and ran on Linux or OSX
    • Is written in a language that runs on the JVM
    • Is tested using appropriate testing mechanisms
    • Has appropriate documentation
    • Don’t include this document in the deliverable
If due to time constraints you cannot deliver a complete solution, please focus on a solution that has fewer features but it is well written and has proper test coverage.