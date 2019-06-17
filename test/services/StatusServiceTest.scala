package services

import java.time.LocalDateTime

import contracts.MeasurementDatabase
import models.{ALERT, Measurement, OK, WARN}
import org.mockito.Mockito._
import org.scalatest.mockito.MockitoSugar
import org.scalatestplus.play.PlaySpec

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class StatusServiceTest extends PlaySpec with MockitoSugar {
  "StatusService" should {
    "Set status to Ok if it is the first measurement and is not above 2000" in new Fixture {
      val okMeasurement = Measurement(1500, someTime)

      private val service = new StatusService(database)
      service.updateStatus(uuid, okMeasurement) mustEqual (OK())

    }
    "Set status to Ok if there were more than three measurements which were not above 2000" in new Fixture {
      val highMeasurement = Measurement(2400, someTime)
      val okMeasurement = Measurement(1500, someTime)

      private val service = new StatusService(database)
      service.updateStatus(uuid, highMeasurement)
      service.updateStatus(uuid, okMeasurement)
      service.updateStatus(uuid, okMeasurement)
      service.updateStatus(uuid, okMeasurement) mustEqual (OK())

    }

    "Set status to WARN if the measurement is above 2000 but previous measurements were below 2000" in new Fixture {
      private val service = new StatusService(database)
      val okMeasurement = Measurement(1500, someTime)
      val highMeasurement = Measurement(2300, someTime)

      service.updateStatus(uuid, okMeasurement)
      service.updateStatus(uuid, okMeasurement)
      service.updateStatus(uuid, okMeasurement)
      service.updateStatus(uuid, highMeasurement) mustEqual (WARN())

    }
    "Set status to WARN if the measurement is below 2000 but previous measurements were above 2000" in new Fixture {
      private val service = new StatusService(database)
      val okMeasurement = Measurement(1500, someTime)
      val highMeasurement = Measurement(2300, someTime)

      service.updateStatus(uuid, okMeasurement)
      service.updateStatus(uuid, okMeasurement)
      service.updateStatus(uuid, okMeasurement)
      service.updateStatus(uuid, highMeasurement)
      service.updateStatus(uuid, okMeasurement) mustEqual (WARN())

    }
    "Set status to ALERT and log alert if there were consecutive three measurements above 2000" in new Fixture {
      val okMeasurement = Measurement(1500, someTime)
      val highMeasurement = Measurement(2300, someTime)

      when(database.logAlert(uuid, highMeasurement)).thenReturn(Future.successful(true))

      private val service = new StatusService(database)
      service.updateStatus(uuid, okMeasurement)
      service.updateStatus(uuid, highMeasurement)
      service.updateStatus(uuid, highMeasurement)
      service.updateStatus(uuid, highMeasurement) mustEqual (ALERT())

      verify(database, times(1)).logAlert(uuid, highMeasurement)

    }
    "Set status to ALERT and log alert if there were consecutive three measurements above 2000 and last two alerts are below 2000" in new Fixture {
      val okMeasurement = Measurement(1500, someTime)
      val highMeasurement = Measurement(2300, someTime)

      when(database.logAlert(uuid, highMeasurement)).thenReturn(Future.successful(true))

      private val service = new StatusService(database)
      service.updateStatus(uuid, okMeasurement)
      service.updateStatus(uuid, okMeasurement)
      service.updateStatus(uuid, highMeasurement)
      service.updateStatus(uuid, highMeasurement)
      service.updateStatus(uuid, highMeasurement)
      service.updateStatus(uuid, okMeasurement)
      service.updateStatus(uuid, okMeasurement) mustEqual (Future.successful(ALERT()))

      verify(database, times(1)).logAlert(uuid, highMeasurement)

    }
  }

  private sealed trait Fixture {
    val someTime = LocalDateTime.of(2019, 2, 14, 2, 2)
    val uuid = "1234"
    val database: MeasurementDatabase = mock[MeasurementDatabase]
  }

}

