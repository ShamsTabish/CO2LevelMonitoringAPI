package controllers

import java.time.LocalDateTime

import javax.inject.Inject
import play.api.libs.json.Json
import play.api.mvc.{AbstractController, AnyContent, ControllerComponents, Request}
import services.MeasurementMatrixService

class MeasurementMatrixController @Inject()(cc: ControllerComponents,
                                            measurementMatrixService: MeasurementMatrixService) extends AbstractController(cc) {

  def getMatrix(uuid: String) = Action { implicit request: Request[AnyContent] =>
    val now = LocalDateTime.now()
    val oneMonthAgo = now.minusDays(30)
    val matrix = measurementMatrixService.calculateMatrix(uuid, oneMonthAgo, now)
    Ok(Json.toJson(matrix))
  }

}
