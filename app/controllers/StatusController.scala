package controllers

import javax.inject._
import models.Measurement
import play.api.libs.json.Json
import play.api.mvc._
import services.StatusService

import scala.concurrent.ExecutionContext


@Singleton
class StatusController @Inject()(cc: ControllerComponents, statusService: StatusService)
                                (implicit val ex: ExecutionContext) extends AbstractController(cc) {

  def getStatus(uuid: String) = Action { implicit request: Request[AnyContent] =>
    val status = statusService.getStatus(uuid)
    Ok(status.toString)
  }

  def getAllAlerts(uuid: String) = Action.async { implicit request: Request[AnyContent] =>
    val allAlertsF = statusService.getAllAlerts(uuid)
    allAlertsF.map { allAlerts =>
      val jsonResult = Json.toJson(allAlerts)(Measurement.allMeasurementWrites)
      Ok(jsonResult)
    }
  }
}
