package config

import java.util.NoSuchElementException

import javax.inject.{Inject, Singleton}
import play.api.Configuration

@Singleton
class Properties @Inject()(configuration: Configuration) {

  lazy val dBConnectionUrl: String = requireProperty("DB_CONNECTION_URL")
  lazy val dBDriver: String = requireProperty("DB_DRIVER")
  lazy val dBUsername: String = requireProperty("DB_USER_NAME")
  lazy val dBPassword: String = requireProperty("DB_PASSWORD")

  private def requireProperty(name: String): String = {
    sys.env.get(name).orElse(None) match {
      case Some(value) => value
      case None => configuration.getOptional[String](name).getOrElse(throw new NoSuchElementException(s"Required property $name is not configured."))
    }
  }
}
