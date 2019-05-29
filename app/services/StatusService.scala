package services

import contracts.MeasurementDatabase
import javax.inject.{Inject, Singleton}
import models._

import scala.collection.immutable.Map
import scala.collection.mutable

case class StatusWithHistory(past3Values: List[Int], status: Status)

@Singleton
class StatusService @Inject()(dataBase: MeasurementDatabase) {
  val ALERT_LEVEL = 2000
  private val statusMap: mutable.Map[String, StatusWithHistory] = mutable.Map.empty[String, StatusWithHistory]

  def getStatus(uuid: String): Status = {
    statusMap.getOrElse(uuid, StatusWithHistory(List.empty, OK())).status
  }

  def updateStatus(uuid: String, measurement: Measurement): Status = {
    synchronized {
      val pastStatus = statusMap.getOrElse(uuid, StatusWithHistory(List.empty, OK()))
      val cO2LevelsHistory = pastStatus.past3Values :+ measurement.co2Level
      val recent3Co2Levels = cO2LevelsHistory.takeRight(3)

      val statusNow: Status = calculateCurrentStatus(measurement, pastStatus, recent3Co2Levels)
      if (statusNow == ALERT())
        dataBase.logAlert(uuid, measurement)
      statusMap.put(uuid, StatusWithHistory(recent3Co2Levels, statusNow))
      statusNow
    }
  }

  private def calculateCurrentStatus(measurement: Measurement, pastStatus: StatusWithHistory, co2LevelsRecent3: List[Int]) = {
    val groupsOfCo2Levels: Map[Boolean, List[Int]] = co2LevelsRecent3.groupBy(_ > 2000)
    val statusNow = groupsOfCo2Levels.keySet.toList match {
      case List(true) => ALERT()
      case List(false) => OK()
      case _ => if (measurement.co2Level > ALERT_LEVEL && pastStatus.status == OK()) {
        WARN()
      } else pastStatus.status
    }
    statusNow
  }
}
