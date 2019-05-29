package services

import java.time.LocalDateTime

import contracts.MeasurementDatabase
import models.{Measurement, MeasurementMatrix}
import org.mockito.Mockito._
import org.scalatest.mockito.MockitoSugar
import org.scalatestplus.play.PlaySpec

class MeasurementMatrixServiceTest extends PlaySpec with MockitoSugar {
  "MeasurementMatrixService" should {
    "calculate correct Max and Average when there are some measurements" in new Fixture {
      val listOfMeasurements = List(
        Measurement(2200, oneDayAgo),
        Measurement(2000, threeDaysAgo),
        Measurement(500, oneDayAgo),
        Measurement(1000, twentyNineDaysAgo)
      )
      when(mockDBService.getMeasurements(uuid, oneMonthAgo, endTime)) thenReturn (listOfMeasurements)
      private val matrixService = new MeasurementMatrixService(mockDBService)
      matrixService.calculateMatrix(uuid, oneMonthAgo, endTime) mustEqual (MeasurementMatrix(2200, 1425.0f))
    }
    "calculate correct Max and Average when there are no records" in new Fixture {
      val listOfMeasurements = List.empty[Measurement]
      when(mockDBService.getMeasurements(uuid, oneMonthAgo, endTime)) thenReturn (listOfMeasurements)
      private val matrixService = new MeasurementMatrixService(mockDBService)
      matrixService.calculateMatrix(uuid, oneMonthAgo, endTime) mustEqual (MeasurementMatrix(0, 0))
    }
  }

  private sealed trait Fixture {
    val mockDBService = mock[MeasurementDatabase]
    val uuid = "105A7DE0-3A7B-4204-B335-4349C9BF812C"
    val endTime = LocalDateTime.of(2019, 2, 1, 2, 2)
    val oneMonthAgo = endTime.minusDays(30)
    val oneDayAgo = endTime.minusDays(1)
    val threeDaysAgo = endTime.minusDays(3)
    val twentyNineDaysAgo = endTime.minusDays(29)


  }

}
