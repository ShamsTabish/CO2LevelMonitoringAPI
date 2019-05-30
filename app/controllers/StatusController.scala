package controllers

import javax.inject._
import models.Measurement
import play.api.libs.json.Json
import play.api.mvc._
import services.StatusService


@Singleton
class StatusController @Inject()(cc: ControllerComponents, statusService: StatusService) extends AbstractController(cc) {

  def getStatus(uuid: String) = Action { implicit request: Request[AnyContent] =>
    val status = statusService.getStatus(uuid)
    Ok(status.toString)
  }

  def getAllAlerts(uuid: String) = Action { implicit request: Request[AnyContent] =>
    val allAlerts = statusService.getAllAlerts(uuid)
    val jsonResult = Json.toJson(allAlerts)(Measurement.allMeasurementWrites)
    Ok(jsonResult)
  }
}
