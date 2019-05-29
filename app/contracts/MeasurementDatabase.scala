package contracts

import java.time.LocalDateTime

import com.google.inject.ImplementedBy
import mockDataAccessLayes.InMemoryStorage
import models.Measurement

@ImplementedBy(classOf[InMemoryStorage])
trait MeasurementDatabase {

  def storeMeasurement(uuid: String, measurement: Measurement): Boolean

  def logAlert(uuid: String, measurement: Measurement): Boolean

  def getMeasurements(uuid: String, start: LocalDateTime, endTime: LocalDateTime): List[Measurement]

  def getAllAlerts(uuid: String): List[Measurement]

}
