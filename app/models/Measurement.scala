package models

import java.time.LocalDateTime

import play.api.libs.functional.syntax._
import play.api.libs.json.Reads._
import play.api.libs.json._


case class Measurement(co2Level: Int, time: LocalDateTime)


object Measurement {
  implicit val measurementWrites = new Writes[Measurement] {
    def writes(measurement: Measurement) = Json.obj(
      "co2" -> measurement.co2Level.toString,
      "time" -> measurement.time.toString
    )
  }

  implicit val measurementReads: Reads[Option[Measurement]] = (
    (JsPath \ "co2").readNullable[String] and
      (JsPath \ "time").readNullable[String]
    ) (Measurement.mayBeMeasurement _)

  private def mayBeMeasurement(co2Level: Option[String], time: Option[String]): Option[Measurement] = {
    if (co2Level.isDefined && time.isDefined) {
      Some(Measurement(co2Level.get.toInt, LocalDateTime.parse(time.get)))
    }
    else None
  }
}

