package services

import java.time.LocalDateTime

import contracts.MeasurementDatabase
import javax.inject.Inject
import models.{Measurement, MeasurementMatrix}

class MeasurementMatrixService @Inject()(dataBase: MeasurementDatabase) {
  def calculateMatrix(uuid: String, startTime: LocalDateTime, endTime: LocalDateTime): MeasurementMatrix = {
    val measurements = dataBase.getMeasurements(uuid, startTime, endTime)
    val maxCo2Level = measurements.foldLeft(0)(findMax)
    val sumOfCo2Levels = measurements.foldLeft(0)((partialSum, measurement) => partialSum + measurement.co2Level)
    val size = measurements.size
    val averageCo2Level = if (size == 0) size else sumOfCo2Levels.toFloat / size
    MeasurementMatrix(maxCo2Level, averageCo2Level)
  }

  private def findMax(maxTillNow: Int, nextMeasurement: Measurement) = {
    if (maxTillNow < nextMeasurement.co2Level) nextMeasurement.co2Level else maxTillNow
  }
}
