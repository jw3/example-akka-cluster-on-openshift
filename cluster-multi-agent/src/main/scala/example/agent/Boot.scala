package example.agent

import akka.actor.{ActorSystem, Address}
import akka.cluster.Cluster
import akka.cluster.client.ClusterClientReceptionist
import akka.stream.ActorMaterializer
import com.typesafe.scalalogging.LazyLogging


object Boot extends App with LazyLogging {
  val clusterName = "ClusterSystem"

  implicit val system = ActorSystem(clusterName)
  implicit val materializer = ActorMaterializer()

  val cluster = Cluster(system)

  system.settings.config.getString("clustering.seeds").split(",").foreach { seed ⇒
    logger.info("operations-injector is joining seed {}", seed)
    cluster.join(
      new Address("akka.tcp", clusterName, seed, 2551)
    )
  }

  Seq(
    "add" → Adder.props(),
    "sub" → Subtractor.props(),
    "mul" → Multiplier.props(),
    "div" → Divider.props()
  ).map { t ⇒
    logger.info("creating '{}' operator", t._1)
    system.actorOf(t._2, t._1)
  }.foreach { ref ⇒
    logger.info("registering {} operator actor", ref.path)
    ClusterClientReceptionist(system).registerService(ref)
  }
}
