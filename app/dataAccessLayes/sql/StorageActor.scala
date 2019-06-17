package dataAccessLayes.sql

import java.sql.ResultSet
import java.time.LocalDateTime

import akka.actor.Actor
import javax.inject.{Inject, Singleton}
import models.actorSignals._
import models.{Measurement, OK, Status}
import play.api.Logger
import services.StatusWithHistory

@Singleton
class StorageActor @Inject()(db: DBCommunicationManager) extends Actor {
  val dbLogger: Logger = Logger("Storage Actor")

  override def receive: Receive = {
    case measurement: StoreMeasurement =>
      sender() ! storeMeasurement(measurement.uuid, measurement.measurement)
    case searchReadings: SearchMeasurements =>
      sender() ! getMeasurements(searchReadings.uuid, searchReadings.start, searchReadings.endTime)
    case storeAlert: StoreAlert =>
      sender() ! logAlert(storeAlert.uuid, storeAlert.measurement)
    case allAlerts: GetAllAlerts =>
      sender() ! getAllAlerts(allAlerts.uuid)
    case historyReq: FetchStatusWithHistory =>
      sender() ! getStatusWithHistory(historyReq.uuid)
    case setHistory: SetStatusWithHistory =>
      sender() ! setStatusWithHistory(setHistory.uuid, setHistory.statusWithHistory)
  }


  private def storeMeasurement(uuid: String, measurement: Measurement): Boolean = {
    val query =s"""insert into Measurement values($uuid,'${measurement.time}',${measurement.co2Level})"""
    db.insert(query).getOrElse(0) > 0
  }


  private def getMeasurements(uuid: String, start: LocalDateTime, endTime: LocalDateTime): List[Measurement] = {
    val query = s"select * from Measurement where uuid = $uuid and capture_time between '$start' and '$endTime'"

    def parseMeasurement(rs: ResultSet) = Measurement(rs.getInt("co2Level"),
      (rs.getTimestamp("capture_time").toLocalDateTime))

    db.select(query, parseMeasurement)
  }


  private def getAllAlerts(uuid: String): List[Measurement] = {
    val query = s"select * from AlertsTable where uuid = $uuid order by capture_time asc"

    def parseAlert(rs: ResultSet) = Measurement(rs.getInt("co2Level"),
      (rs.getTimestamp("capture_time").toLocalDateTime))

    db.select(query, parseAlert)
  }

  private def logAlert(uuid: String, measurement: Measurement): Boolean = {
    val query =s"""insert into AlertsTable values($uuid,'${measurement.time}',${measurement.co2Level})"""
    db.insert(query).getOrElse(0) > 0
  }

  private def getStatusWithHistory(uuid: String): StatusWithHistory = {
    val query = s"select * from StatusWithHistory where uuid = $uuid "
    db.select[Option[StatusWithHistory]](query, parseStatusWithHistory)
      .filter(_ != None).map(_.get)
      .headOption.getOrElse(StatusWithHistory(List.empty, OK()))
  }

  private def setStatusWithHistory(uuid: String, statusWithHistory: StatusWithHistory): Boolean = {
    val co2HistoryAsString = statusWithHistory.past3Values.mkString(", ")
    val query = s"insert into StatusWithHistory values ($uuid,'$co2HistoryAsString','${statusWithHistory.status}');"
    db.insert(query).getOrElse(0) > 0
  }

  private def parseStatusWithHistory(rs: ResultSet) = {
    val co2History = rs.getString("co2History")
    val historyOfValues = if (null != co2History) {
      co2History.split(", ").toList.map(_.toInt)
    } else List.empty
    val statusString = rs.getString("status")
    Status(statusString).map(status =>
      StatusWithHistory(historyOfValues, status)
    )
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
