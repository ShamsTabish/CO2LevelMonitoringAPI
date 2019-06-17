package models.actorSignals

import java.time.LocalDateTime

import models.Measurement
import services.StatusWithHistory

trait Signals

case class StoreMeasurement(uuid: String, measurement: Measurement) extends Signals

case class SearchMeasurements(uuid: String, start: LocalDateTime, endTime: LocalDateTime) extends Signals

case class GetAllAlerts(uuid: String) extends Signals

case class StoreAlert(uuid: String, measurement: Measurement) extends Signals

case class FetchStatusWithHistory(uuid: String) extends Signals

case class SetStatusWithHistory(uuid: String, statusWithHistory: StatusWithHistory) extends Signals