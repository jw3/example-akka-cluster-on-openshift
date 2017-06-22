package example.agent

import akka.actor.{ActorPath, ActorSystem}
import akka.cluster.client.{ClusterClient, ClusterClientSettings}
import akka.stream.ActorMaterializer
import akka.util.Timeout
import com.typesafe.scalalogging.LazyLogging

import scala.concurrent.duration.DurationInt


object Boot extends App with LazyLogging {
  val clusterName = "ClusterSystem"

  implicit val system = ActorSystem("cluster-client")
  implicit val materializer = ActorMaterializer()
  implicit val timeout = Timeout(10 seconds)

  val initialContacts = system.settings.config.getString("clustering.seeds").split(",").map { seed ⇒
    ActorPath.fromString(s"akka.tcp://$clusterName@$seed:2551/system/receptionist")
  }.toSet

  logger.info(s"client initial contacts: ${initialContacts.mkString(",")}")

  val client = system.actorOf(ClusterClient.props(
    ClusterClientSettings(system).withInitialContacts(initialContacts)
  ), "client")

  HttpInterface.up(client, "0.0.0.0", 9000)
}
