package services

import contracts.MeasurementDatabase
import javax.inject.{Inject, Singleton}
import models._

import scala.concurrent.{ExecutionContext, Future}

case class StatusWithHistory(past3Values: List[Int], status: Status)

@Singleton
class StatusService @Inject()(dataBase: MeasurementDatabase)
                             (implicit val ec: ExecutionContext) {
  val ALERT_LEVEL = 2000

  def getStatus(uuid: String): Future[Status] = {
    dataBase.getStatusWithHistory(uuid).map(_.status)
  }

  def getAllAlerts(uuid: String): Future[List[Measurement]] = {
    dataBase.getAllAlerts(uuid)
  }

  def updateStatus(uuid: String, measurement: Measurement): Future[Status] = {
    dataBase.getStatusWithHistory(uuid).map {
      statusWithHistory =>
        val recent3Co2Levels = (statusWithHistory.past3Values :+ measurement.co2Level).takeRight(3)
        val currentStatus = calculateCurrentStatus(measurement, statusWithHistory, recent3Co2Levels)
        if (currentStatus == ALERT())
          dataBase.logAlert(uuid, measurement)
        dataBase.setStatusWithHistory(uuid, StatusWithHistory(recent3Co2Levels, currentStatus))
        currentStatus
    }
  }

  private def calculateCurrentStatus(measurement: Measurement,
                                     pastStatus: StatusWithHistory, co2LevelsRecent3: List[Int]) = {
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
