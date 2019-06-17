USE co2Status;

create TABLE IF NOT EXISTS Measurement (
    uuid Integer(30) NOT NULL,
    capture_time timestamp NOT NULL,
    co2Level Integer(5) NOT NULL
);


create TABLE IF NOT EXISTS  AlertsTable(
    uuid Integer(30) NOT NULL,
    capture_time timestamp NOT NULL,
    co2Level Integer(5) NOT NULL
);

create TABLE IF NOT EXISTS  StatusWithHistory(
    uuid Integer(30) NOT NULL,
    co2History Varchar(25),
    status Varchar(10)
);