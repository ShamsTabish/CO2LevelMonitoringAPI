package contracts

import java.time.LocalDateTime

import com.google.inject.ImplementedBy
import dataAccessLayes.InMemoryStorage
import models.Measurement
import services.StatusWithHistory

import scala.concurrent.Future

@ImplementedBy(classOf[InMemoryStorage])
trait MeasurementDatabase {

  def storeMeasurement(uuid: String, measurement: Measurement): Future[Boolean]

  def logAlert(uuid: String, measurement: Measurement): Future[Boolean]

  def getMeasurements(uuid: String, start: LocalDateTime, endTime: LocalDateTime): Future[List[Measurement]]

  def getAllAlerts(uuid: String): Future[List[Measurement]]

  def getStatusWithHistory(uuid: String): Future[StatusWithHistory]

  def setStatusWithHistory(uuid: String, statusWithHistory: StatusWithHistory): Future[Boolean]

}
