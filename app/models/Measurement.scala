package models

import java.time.LocalDateTime

import play.api.libs.functional.syntax._
import play.api.libs.json.Reads._
import play.api.libs.json._

import scala.util.Try


case class Measurement(co2Level: Int, time: LocalDateTime)

object Measurement {
  implicit val measurementWrites = new Writes[Measurement] {
    def writes(measurement: Measurement) = Json.obj(
      "co2" -> measurement.co2Level.toString,
      "time" -> measurement.time.toString
    )
  }

  implicit val allMeasurementWrites = new Writes[List[Measurement]] {
    def writes(measurements: List[Measurement]) = {
      if (measurements.isEmpty)
        Json.parse("{}")
      else
        convertMeasurementListToJson(measurements)
    }
  }

  implicit val measurementReads: Reads[Option[Measurement]] = (
    (JsPath \ "co2").readNullable[String] and
      (JsPath \ "time").readNullable[String]
    ) (Measurement.mayBeMeasurement _)

  private def convertMeasurementListToJson(measurements: List[Measurement]): JsValue = {
    val startTime = measurements.map(_.time).max((a: LocalDateTime, b: LocalDateTime) => a.compareTo(b))
    val endTime = measurements.map(_.time).min((a: LocalDateTime, b: LocalDateTime) => a.compareTo(b))
    val status = measurements.map(_.co2Level)
    val measurementsString = status.zipWithIndex.map(e => s""""mesurement${e._2 + 1}"  :  "${e._1}"""").mkString(",\n")

    JsArray().append(Json.parse(
      s"""{
         |"startTime" : "${startTime.toString}",
         |"endTime" : "${endTime.toString}",
         |$measurementsString
         |}""".
        stripMargin
    ))
  }

  private def mayBeMeasurement(co2Level: Option[String], time: Option[String]): Option[Measurement] = {
    if (co2Level.isDefined && time.isDefined) {
      val getTime = Try(Some(LocalDateTime.parse(time.get))).getOrElse(None)
      getTime.map(t => Measurement(co2Level.get.toInt, t))
    }
    else None
  }
}

