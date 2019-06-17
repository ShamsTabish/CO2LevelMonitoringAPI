package services

import contracts.MeasurementDatabase
import javax.inject.Inject
import models.Measurement

import scala.concurrent.Future

class StorageService @Inject()(dataBase: MeasurementDatabase) {
  def saveMeasurement(uuid: String, measurement: Measurement): Future[Boolean] = {
    dataBase.storeMeasurement(uuid, measurement)
  }

}
