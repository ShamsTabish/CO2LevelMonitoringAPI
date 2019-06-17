package controllers

import java.time.LocalDateTime

import javax.inject.Inject
import play.api.libs.json.Json
import play.api.mvc.{AbstractController, AnyContent, ControllerComponents, Request}
import services.MeasurementMatrixService

import scala.concurrent.ExecutionContext

class MeasurementMatrixController @Inject()(cc: ControllerComponents, measurementMatrixService: MeasurementMatrixService)
                                           (implicit val ec:ExecutionContext)extends AbstractController(cc) {

  def getMatrix(uuid: String) = Action.async { implicit request: Request[AnyContent] =>
    val now = LocalDateTime.now()
    val oneMonthAgo = now.minusDays(30)
    val matrixF = measurementMatrixService.calculateMatrix(uuid, oneMonthAgo, now)
    matrixF.map(matrix => Ok(Json.toJson(matrix)))
  }

}
