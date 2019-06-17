package controllers

import javax.inject.{Inject, Singleton}
import models.Measurement
import play.api.libs.json.Json
import play.api.mvc._
import services.StorageService

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class MeasurementController @Inject()(cc: ControllerComponents, storageService: StorageService)
                                     (implicit val ec: ExecutionContext) extends AbstractController(cc) {

  def collect(uuid: String) = Action.async { implicit request: Request[AnyContent] =>
    val mayBeMeasurement = request.body.asJson.flatMap(_.as[Option[Measurement]])
    mayBeMeasurement match {
      case None => Future.successful(BadRequest(s"Invalid Data: ${request.body.asJson}"))
      case Some(measurement) => tryToStoreTheMeasurement(uuid, measurement)
    }
  }


  private def tryToStoreTheMeasurement(uuid: String, measurement: Measurement): Future[Result] = {
    val futureStatus = storageService.saveMeasurement(uuid, measurement)
    futureStatus.map(status => if (status) Ok(Json.toJson(measurement))
    else InternalServerError("Some thing went wrong, we could not store the measurement!")
    )
  }
}
