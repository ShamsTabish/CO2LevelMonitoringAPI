package dataAccessLayes

import java.time.LocalDateTime

import akka.actor.{ActorSystem, Props}
import akka.pattern.ask
import akka.util.Timeout
import config.Properties
import contracts.MeasurementDatabase
import dataAccessLayes.sql.{DBCommunicationManager, StorageActor}
import javax.inject.Inject
import models.Measurement
import models.actorSignals._
import services.StatusWithHistory

import scala.concurrent.duration._
import scala.concurrent.{ExecutionContext, Future}

class SQLStorage @Inject()(db: DBCommunicationManager, system: ActorSystem, properties: Properties)
                          (implicit val ec: ExecutionContext) extends MeasurementDatabase {

  val storageProps = Props(classOf[StorageActor], db)
  val storageActor = system.actorOf(storageProps)
  private implicit val timeout = Timeout(properties.dbActorTimeout seconds)

  override def storeMeasurement(uuid: String, measurement: Measurement): Future[Boolean] = {
    val futureStatus = storageActor ? StoreMeasurement(uuid, measurement)
    futureStatus.map(_.asInstanceOf[Boolean])
  }

  override def logAlert(uuid: String, measurement: Measurement): Future[Boolean] =
    (storageActor ? StoreAlert(uuid, measurement)).map(_.asInstanceOf[Boolean])


  override def getMeasurements(uuid: String, start: LocalDateTime, endTime: LocalDateTime): Future[List[Measurement]] =
    (storageActor ? SearchMeasurements(uuid, start, endTime)).map(_.asInstanceOf[List[Measurement]])


  override def getAllAlerts(uuid: String): Future[List[Measurement]] =
    (storageActor ? GetAllAlerts(uuid)).map(_.asInstanceOf[List[Measurement]])


  override def getStatusWithHistory(uuid: String): Future[StatusWithHistory] =
    (storageActor ? FetchStatusWithHistory(uuid)).map(_.asInstanceOf[StatusWithHistory])


  override def setStatusWithHistory(uuid: String, statusWithHistory: StatusWithHistory): Future[Boolean] =
    (storageActor ? SetStatusWithHistory(uuid, statusWithHistory: StatusWithHistory)).map(_.asInstanceOf[Boolean])
}
