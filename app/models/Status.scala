package models

sealed trait Status

case class OK() extends Status

case class WARN() extends Status

case class ALERT() extends Status

