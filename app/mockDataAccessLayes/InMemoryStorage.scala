package mockDataAccessLayes

import java.time.LocalDateTime

import contracts.MeasurementDatabase
import javax.inject.Singleton
import models.Measurement

import scala.collection.mutable
import scala.util.Try

@Singleton
class InMemoryStorage extends MeasurementDatabase {

  override def storeMeasurement(uuid: String, measurement: Measurement): Boolean = {
    synchronized {
      Try {
        val measurements = Database.measurementMap.getOrElse(uuid, List.empty[Measurement])
        val updatedMeasurements = measurements :+ measurement
        Database.measurementMap.put(uuid, updatedMeasurements)
        true
      }.getOrElse(false)
    }

  }

  override def getMeasurements(uuid: String, start: LocalDateTime, endTime: LocalDateTime): List[Measurement] =
    Database.measurementMap.getOrElse(uuid, List.empty).filter(m => m.time.isAfter(start)).filter(m => m.time.isBefore(endTime))

}

object Database {
  val measurementMap: mutable.Map[String, List[Measurement]] = mutable.Map.empty[String, List[Measurement]]
}
