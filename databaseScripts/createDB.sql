USE co2Status;

CREATE TABLE IF NOT EXISTS Measurement (
    uuid Integer(30) NOT NULL,
    capture_time timestamp NOT NULL,
    co2Level Integer(5) NOT NULL
);


CREATE TABLE IF NOT EXISTS  AlertsTable(
    uuid Integer(30) NOT NULL,
    capture_time timestamp NOT NULL,
    co2Level Integer(5) NOT NULL
);