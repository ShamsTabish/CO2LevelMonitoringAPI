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
    "should be convertible from Json" in {
      val maybeMeasurement = Json.parse(jsonMeasurement).as[Option[Measurement]]
      maybeMeasurement mustEqual (Some(sampleMeasurement))
    }
  }
}
