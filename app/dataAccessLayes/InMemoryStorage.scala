package dataAccessLayes

import java.time.LocalDateTime

import contracts.MeasurementDatabase
import javax.inject.Singleton
import models.{Measurement, OK, Status}
import services.StatusWithHistory

import scala.collection.mutable
import scala.concurrent.Future
import scala.util.Try

@Singleton
class InMemoryStorage extends MeasurementDatabase {
  private val measurementMap: mutable.Map[String, List[Measurement]] = mutable.Map.empty[String, List[Measurement]]
  private val alertMap: mutable.Map[String, List[Measurement]] = mutable.Map.empty[String, List[Measurement]]
  private val statusMap: mutable.Map[String, StatusWithHistory] = mutable.Map.empty[String, StatusWithHistory]

  override def storeMeasurement(uuid: String, measurement: Measurement): Future[Boolean] = {
    val status = synchronized {
      Try {
        val measurements = measurementMap.getOrElse(uuid, List.empty[Measurement])
        val updatedMeasurements = measurements :+ measurement
        measurementMap.put(uuid, updatedMeasurements)
        true
      }.getOrElse(false)
    }
    Future.successful(status)
  }

  override def getMeasurements(uuid: String, start: LocalDateTime, endTime: LocalDateTime): Future[List[Measurement]] = {
    val measurements = measurementMap.getOrElse(uuid, List.empty).filter(m => m.time.isAfter(start)).filter(m => m.time.isBefore(endTime))
    Future.successful(measurements)
  }

  override def getAllAlerts(uuid: String): Future[List[Measurement]] = Future.successful(measurementMap.getOrElse(uuid, List.empty))

  override def logAlert(uuid: String, measurement: Measurement): Future[Boolean] = {
    val status = synchronized {
      Try {
        val alerts = alertMap.getOrElse(uuid, List.empty[Measurement])
        val updatedAlerts = alerts :+ measurement
        alertMap.put(uuid, updatedAlerts)
        true
      }.getOrElse(false)
    }
    Future.successful(status)
  }

  def getStatusWithHistory(uuid: String): Future[StatusWithHistory] = {
    Future.successful(statusMap.getOrElse(uuid, StatusWithHistory(List.empty, OK())))
  }

  def setStatusWithHistory(uuid: String, statusWithHistory: StatusWithHistory): Future[Boolean] = {
    Future.successful(statusMap.put(uuid, statusWithHistory).isDefined)
  }

}
