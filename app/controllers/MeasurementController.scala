package controllers

import javax.inject.{Inject, Singleton}
import models.Measurement
import play.api.libs.json.Json
import play.api.mvc.{AbstractController, AnyContent, ControllerComponents, Request}
import services.StorageService

@Singleton
class MeasurementController @Inject()(cc: ControllerComponents,
                                      storageService: StorageService) extends AbstractController(cc) {

  def collect(uuid: String) = Action { implicit request: Request[AnyContent] =>
    val mayBeMeasurement = request.body.asJson.flatMap(_.as[Option[Measurement]])
    mayBeMeasurement match {
      case None => BadRequest(s"Invalid Data: $mayBeMeasurement")
      case Some(measurement) => tryToStoreTheMeasurement(uuid, measurement)
    }
  }


  private def tryToStoreTheMeasurement(uuid: String, measurement: Measurement) = {
    if (storageService.saveMeasurement(uuid, measurement)) Ok(Json.toJson(measurement))
    else InternalServerError("Some thing went wrong, we could not store the measurement!")
  }
}
