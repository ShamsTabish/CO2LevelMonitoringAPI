package controllers

import models.MeasurementMatrix
import org.mockito.ArgumentMatchers
import org.mockito.Mockito._
import org.scalatest.mockito.MockitoSugar
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerTest
import play.api.libs.json.{JsValue, Json}
import play.api.test.Helpers.{GET, status, stubControllerComponents, _}
import play.api.test.{FakeRequest, Helpers, Injecting}
import services.MeasurementMatrixService


class MeasurementMatrixControllerTest extends PlaySpec with GuiceOneAppPerTest with Injecting with MockitoSugar {

  "MeasurementMatrixController GET Matrix" should {

    "return measurement Json with status code as Ok when measurement stored." in new Fixture {
      private val matrix = MeasurementMatrix(2100, 1600.4f)
      private val jsonMatrix: JsValue = Json.toJson(matrix)
      when(mockMeasurementMatrixService
        .calculateMatrix(ArgumentMatchers.any(), ArgumentMatchers.any(), ArgumentMatchers.any())
      ).thenReturn(matrix)
      val controller = new MeasurementMatrixController(stubControllerComponents(), mockMeasurementMatrixService)
      val fakeRequest = FakeRequest(GET, s"/api/v1/sensors/$uuid/mesurements")
      val matrixResult = controller.getMatrix(uuid)(fakeRequest)
      status(matrixResult) mustBe OK
      Helpers.contentAsString(matrixResult) mustBe jsonMatrix.toString()
    }

  }

  private sealed trait Fixture {
    val uuid = "1234"
    val mockMeasurementMatrixService = mock[MeasurementMatrixService]

  }

}
