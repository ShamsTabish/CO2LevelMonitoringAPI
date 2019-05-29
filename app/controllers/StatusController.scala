package controllers

import javax.inject._
import play.api.mvc._
import services.StatusService


@Singleton
class StatusController @Inject()(cc: ControllerComponents, statusService: StatusService) extends AbstractController(cc) {

  def getStatus(uuid: String) = Action { implicit request: Request[AnyContent] =>
    val status = statusService.getStatus(uuid)

    println(status.toString)
    Ok(status.toString)
  }
}
