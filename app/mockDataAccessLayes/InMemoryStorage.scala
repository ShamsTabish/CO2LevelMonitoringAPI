package mockDataAccessLayes

import java.time.LocalDateTime

import contracts.MeasurementDatabase
import javax.inject.Singleton
import models.Measurement

import scala.collection.mutable
import scala.util.Try

@Singleton
class InMemoryStorage extends MeasurementDatabase {
  private val measurementMap: mutable.Map[String, List[Measurement]] = mutable.Map.empty[String, List[Measurement]]
  private val alertMap: mutable.Map[String, List[Measurement]] = mutable.Map.empty[String, List[Measurement]]

  override def storeMeasurement(uuid: String, measurement: Measurement): Boolean = {
    synchronized {
      Try {
        val measurements = measurementMap.getOrElse(uuid, List.empty[Measurement])
        val updatedMeasurements = measurements :+ measurement
        measurementMap.put(uuid, updatedMeasurements)
        true
      }.getOrElse(false)
    }

  }

  override def getMeasurements(uuid: String, start: LocalDateTime, endTime: LocalDateTime): List[Measurement] =
    measurementMap.getOrElse(uuid, List.empty).filter(m => m.time.isAfter(start)).filter(m => m.time.isBefore(endTime))

  override def getAllAlerts(uuid: String): List[Measurement] = measurementMap.getOrElse(uuid, List.empty)

  override def logAlert(uuid: String, measurement: Measurement): Boolean = {
    synchronized {
      Try {
        val alerts = alertMap.getOrElse(uuid, List.empty[Measurement])
        val updatedAlerts = alerts :+ measurement
        alertMap.put(uuid, updatedAlerts)
        true
      }.getOrElse(false)
    }
  }
}
