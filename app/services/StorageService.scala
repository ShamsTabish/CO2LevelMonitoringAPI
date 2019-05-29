package services

import contracts.MeasurementDatabase
import javax.inject.Inject
import models.Measurement

class StorageService @Inject()(dataBase: MeasurementDatabase) {
  def saveMeasurement(uuid: String, measurement: Measurement) = {
    dataBase.storeMeasurement(uuid, measurement)
  }

}
