package dataAccessLayes

import java.sql.ResultSet
import java.time.LocalDateTime

import akka.actor.Actor
import contracts.MeasurementDatabase
import javax.inject.{Inject, Singleton}
import models.Measurement
import models.actorSignals.{GetAllAlerts, SearchMeasurements, StoreAlert, StoreMeasurement}
import play.api.Logger

@Singleton
class StorageActor @Inject()(db: DBCommunicationManager) extends Actor with MeasurementDatabase {
  val dbLogger: Logger = Logger("DB Actor")

  override def receive: Receive = {
    case measurement: StoreMeasurement => storeMeasurement(measurement.uuid, measurement.measurement)
    case searchReadings: SearchMeasurements => getMeasurements(searchReadings.uuid, searchReadings.start, searchReadings.endTime)
    case storeAlert: StoreAlert => logAlert(storeAlert.uuid, storeAlert.measurement)
    case allAlerts: GetAllAlerts => getAllAlerts(allAlerts.uuid)

  }


  override def storeMeasurement(uuid: String, measurement: Measurement): Boolean = {
    val query =s"""insert into Measurement values('$uuid','${measurement.time}',${measurement.co2Level})"""
    db.insert(query).isDefined
  }


  override def getMeasurements(uuid: String, start: LocalDateTime, endTime: LocalDateTime): List[Measurement] = {
    val query = s"select * from Measurement where uuid like $uuid and capture_time between '$start' and '$endTime'"

    def parseMeasurement(rs: ResultSet) = Measurement(rs.getInt("co2Level"),
      (rs.getTimestamp("capture_time").toLocalDateTime))

    db.select(query, parseMeasurement)
  }


  override def getAllAlerts(uuid: String): List[Measurement] = {
    val query = s"select * from AlertsTable where uuid like $uuid order by capture_time asc"

    def parseAlert(rs: ResultSet) = Measurement(rs.getInt("co2Level"),
      (rs.getTimestamp("capture_time").toLocalDateTime))

    db.select(query, parseAlert)
  }

  override def logAlert(uuid: String, measurement: Measurement): Boolean = {
    val query =s"""insert into AlertsTable values('$uuid','${measurement.time}',${measurement.co2Level})"""
    db.insert(query).isDefined
  }

  override def preStart(): Unit = {
    super.preStart()
    db.connectToDB()
  }

  override def postRestart(reason: Throwable): Unit = {
    super.postRestart(reason)
    dbLogger.error("System Restarted successfully, recovered from ", reason)
    db.connectToDB()
  }

}
