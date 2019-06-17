package models

sealed trait Status {
  override def toString = {
    val stringRepresentation = this match {
      case ALERT() => "ALERT"
      case WARN() => "WARN"
      case OK() => "OK"
    }

    s"""
       |{
       | "status" : "$stringRepresentation"
       |}
    """.stripMargin
  }
}

object Status {
  def apply(status: String): Option[Status] = status match {
    case "ALERT" => Some(ALERT())
    case "WARN" => Some(WARN())
    case "OK" => Some(OK())
    case _ => None
  }
}

case class OK() extends Status

case class WARN() extends Status

case class ALERT() extends Status

