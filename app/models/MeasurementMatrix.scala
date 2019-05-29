package models

import play.api.libs.json.{Json, Writes}

case class MeasurementMatrix(maxCo2Level: Int, averageCo2Level: Float)

object MeasurementMatrix {
  implicit val measurementWrites = new Writes[MeasurementMatrix] {
    def writes(measurementMatrix: MeasurementMatrix) = Json.obj(
      "maxLast30Days" -> measurementMatrix.maxCo2Level,
      "avgLast30Days" -> measurementMatrix.averageCo2Level
    )
  }
}
