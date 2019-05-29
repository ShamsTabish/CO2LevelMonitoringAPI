package models

sealed trait Status {
  override def toString = {
    val stringRepresentation = this match {
      case ALERT() => "ALERT"
      case WARN() => "WARN"
      case OK() => "OK"
    }
    stringRepresentation
    s"""
       |{
       | "status" : "$stringRepresentation"
       |}
    """.stripMargin
  }
}

case class OK() extends Status

case class WARN() extends Status

case class ALERT() extends Status

