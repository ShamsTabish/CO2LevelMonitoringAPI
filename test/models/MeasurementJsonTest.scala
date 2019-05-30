package models

import java.time.LocalDateTime

import org.scalatestplus.play.PlaySpec
import play.api.libs.json.Json

class MeasurementJsonTest extends PlaySpec {
  val jsonMeasurement = """{"co2":"23","time":"2019-02-14T10:10"}"""
  val sampleMeasurement = Measurement(23, LocalDateTime.of(2019, 2, 14, 10, 10))
  "Measurement" should {
    "be convertible to Json Object" in {
      Json.toJson(sampleMeasurement).toString() mustEqual jsonMeasurement
    }
    "should be convertible from Json" in {
      val maybeMeasurement = Json.parse(jsonMeasurement).as[Option[Measurement]]
      maybeMeasurement mustEqual (Some(sampleMeasurement))
    }
    "should return None if input is missing some field" in {
      val invalidJsonMeasurement = """{"other":"23","time":"2019-02-14T10:10"}"""
      val maybeMeasurement = Json.parse(invalidJsonMeasurement).as[Option[Measurement]]
      maybeMeasurement mustEqual (None)
    }
    "should return None if invalid date passed" in {
      val invalidJsonMeasurement = """{"co2":"23","time":"201-22A10-10"}"""
      val maybeMeasurement = Json.parse(invalidJsonMeasurement).as[Option[Measurement]]
      maybeMeasurement mustEqual (None)
    }
  }
}
