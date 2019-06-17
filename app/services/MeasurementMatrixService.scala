package services

import java.time.LocalDateTime

import contracts.MeasurementDatabase
import javax.inject.Inject
import models.{Measurement, MeasurementMatrix}

import scala.concurrent.{ExecutionContext, Future}

class MeasurementMatrixService @Inject()(dataBase: MeasurementDatabase)(implicit val ex: ExecutionContext) {
  def calculateMatrix(uuid: String, startTime: LocalDateTime, endTime: LocalDateTime): Future[MeasurementMatrix] = {
    val measurementsF: Future[List[Measurement]] = dataBase.getMeasurements(uuid, startTime, endTime)
    val maxCo2LevelF = measurementsF.map(_.foldLeft(0)(findMax))
    val sumOfCo2LevelsF = measurementsF.map(_.foldLeft(0)((partialSum, measurement) => partialSum + measurement.co2Level))
    val sizeF = measurementsF.map(_.size)

    for {
      maxCo2Level <- maxCo2LevelF
      sumOfCo2Levels <- sumOfCo2LevelsF
      size <- sizeF

      averageCo2Level = if (size == 0) size else sumOfCo2Levels.toFloat / size
    } yield MeasurementMatrix(maxCo2Level, averageCo2Level)

  }

  private def findMax(maxTillNow: Int, nextMeasurement: Measurement) = {
    if (maxTillNow < nextMeasurement.co2Level) nextMeasurement.co2Level else maxTillNow
  }
}
