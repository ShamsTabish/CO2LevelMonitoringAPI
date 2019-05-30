package controllers

import models.Measurement
import org.mockito.Mockito._
import org.scalatest.mockito.MockitoSugar
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerTest
import play.api.libs.json.Json
import play.api.test.Helpers.{POST, status, stubControllerComponents, _}
import play.api.test.{FakeRequest, Helpers, Injecting}
import services.StorageService


class MeasurementControllerTest extends PlaySpec with GuiceOneAppPerTest with Injecting with MockitoSugar {

  "MeasurementController POST measurement" should {

    "return measurement Json with status code as Ok when measurement stored." in {
      val uuid = "1234"
      val mockStorageService = mock[StorageService]
      val jsonRequestBody = Json.parse( """{"co2":"2300","time":"2019-05-14T10:10"}""")
      when(mockStorageService.saveMeasurement(uuid, jsonRequestBody.as[Option[Measurement]].get)).thenReturn(true)
      val controller = new MeasurementController(stubControllerComponents(), mockStorageService)
      val fakeRequest = FakeRequest(POST, s"/api/v1/sensors/$uuid/mesurements").withJsonBody(jsonRequestBody)
      val measurementResult = controller.collect(uuid)(fakeRequest)
      status(measurementResult) mustBe OK
      Helpers.contentAsString(measurementResult) mustBe (jsonRequestBody.toString())
    }
    "return BadRequest status code when in appropriate data sent for storage" in {
      val uuid = "1234"
      val mockStorageService = mock[StorageService]
      val jsonRequestBody = Json.parse( """{"N":"2300","time":"2019-05-14T10:10"}""")
      val controller = new MeasurementController(stubControllerComponents(), mockStorageService)
      val fakeRequest = FakeRequest(POST, s"/api/v1/sensors/$uuid/mesurements").withJsonBody(jsonRequestBody)
      val measurementResult = controller.collect(uuid)(fakeRequest)
      status(measurementResult) mustBe BAD_REQUEST
      Helpers.contentAsString(measurementResult) mustBe ("""Invalid Data: Some({"N":"2300","time":"2019-05-14T10:10"})""")
    }
    "return status code 500 data storage fails" in {
      val uuid = "1234"
      val mockStorageService = mock[StorageService]
      val jsonRequestBody = Json.parse( """{"co2":"2300","time":"2019-05-14T10:10"}""")
      when(mockStorageService.saveMeasurement(uuid, jsonRequestBody.as[Option[Measurement]].get)).thenReturn(false)
      val controller = new MeasurementController(stubControllerComponents(), mockStorageService)
      val fakeRequest = FakeRequest(POST, s"/api/v1/sensors/$uuid/mesurements").withJsonBody(jsonRequestBody)
      val measurementResult = controller.collect(uuid)(fakeRequest)
      status(measurementResult) mustBe INTERNAL_SERVER_ERROR
      Helpers.contentAsString(measurementResult) mustBe ("Some thing went wrong, we could not store the measurement!")
    }
  }
}