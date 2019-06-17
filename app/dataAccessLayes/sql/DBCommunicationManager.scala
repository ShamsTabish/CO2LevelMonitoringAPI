package dataAccessLayes.sql

import java.sql.{Connection, DriverManager, ResultSet}

import config.Properties
import javax.inject.{Inject, Singleton}
import play.api.Logger

import scala.util.{Failure, Success, Try}

@Singleton
class DBCommunicationManager @Inject()(properties: Properties) {
  var connection: Connection = _
  val dbLogger: Logger = Logger("DBCommunicationManager")
  val url = properties.dBConnectionUrl
  val driver = properties.dBDriver
  val username = properties.dBUsername
  val password = properties.dBPassword

  private[dataAccessLayes] def connectToDB() = {
    if (connection != null) {
      try {
        Class.forName(driver)
        connection = DriverManager.getConnection(url, username, password)
      } catch {
        case exception:Throwable => dbLogger.error("Failed to connect to DB.", exception)
      }
    }
  }

  private[dataAccessLayes] def disconnectFromDB(): Unit = {
    if (connection != null)
      connection.close()
  }


  private[sql] def select[T](query: String, resultSetMapper: (ResultSet) => T): List[T] = {
    synchronized {
      val probableData = Try {
        val statement = connection.createStatement
        val rs = statement.executeQuery(query)
        iterateResultSet(rs, List.empty, resultSetMapper)
      }
      val records = probableData match {
        case Success(results) => results
        case Failure(error) => {
          dbLogger.error(s"Failed to fetch info from DB, for query: $query.", error)
          List.empty
        }
      }
      records
    }
  }

  private[sql] def insert[T](query: String): Option[Int] = {
    synchronized {
      val probableCount = Try {
        val statement = connection.createStatement
        statement.executeUpdate(query)
      }

      val records = probableCount match {
        case Success(countOfRowsInserted) => Some(countOfRowsInserted)
        case Failure(error) => {
          dbLogger.error(s"Failed to insert into DB, for query: $query.", error)
          None
        }
      }
      records
    }
  }

  private def iterateResultSet[T](rs: ResultSet, results: List[T], resultSetToObj: (ResultSet) => T): List[T] = {
    if (rs.next()) {
      val obj = resultSetToObj(rs)
      val updatedResults = results :+ obj
      iterateResultSet(rs, updatedResults, resultSetToObj)
    } else results
  }
}
