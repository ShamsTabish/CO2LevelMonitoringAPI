package controllers

import java.time.LocalDateTime

import models.{Measurement, MeasurementMatrix}
import org.mockito.ArgumentMatchers
import org.mockito.Mockito._
import org.mockito.verification.VerificationMode
import org.scalatest.mockito.MockitoSugar
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerTest
import play.api.libs.json.{JsValue, Json}
import play.api.test.Helpers.{GET, status, stubControllerComponents, _}
import play.api.test.{FakeRequest, Helpers, Injecting}
import services.StatusService


class StatusControllerTest extends PlaySpec with GuiceOneAppPerTest with Injecting with MockitoSugar {

  "StatusController GET AllAlerts" should {
    "return all alerts Json while it calls status service to fetch all alerts." in new Fixture {
      private val measurement1 = Measurement(2344, LocalDateTime.now())
      private val measurements = List(measurement1)
      when(mockStatusService.getAllAlerts(uuid)).thenReturn(measurements)
      val controller = new StatusController(stubControllerComponents(), mockStatusService)
      val fakeRequest = FakeRequest(GET, s"/api/v1/sensors/$uuid/alerts")
      val statusResult = controller.getAllAlerts(uuid)(fakeRequest)
      verify(mockStatusService, times(1)).getAllAlerts(uuid)
      status(statusResult) mustBe OK
      val expectedResult = Json.toJson(measurements)(Measurement.allMeasurementWrites)

      Helpers.contentAsString(statusResult).trim mustBe expectedResult.toString()
    }

  }
  "StatusController GET current status for sensor" should {

    "return current status Json while it calls status service." in new Fixture {
      when(mockStatusService.getStatus(uuid)).thenReturn(models.OK())
      val controller = new StatusController(stubControllerComponents(), mockStatusService)
      val fakeRequest = FakeRequest(GET, s"/api/v1/sensors/$uuid")
      val statusResult = controller.getStatus(uuid)(fakeRequest)
      verify(mockStatusService, times(1)).getStatus(uuid)
      status(statusResult) mustBe OK
      val expectedResult =
        """{
          | "status" : "OK"
          |}
          |""".stripMargin.trim

      Helpers.contentAsString(statusResult).trim mustBe expectedResult
    }

  }

  private sealed trait Fixture {
    val uuid = "1234"
    val mockStatusService = mock[StatusService]

  }

}