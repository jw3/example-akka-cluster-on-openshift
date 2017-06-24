package example.agent

import akka.actor.{ActorSystem, Address, Props}
import akka.cluster.Cluster
import akka.cluster.client.ClusterClientReceptionist
import com.typesafe.scalalogging.LazyLogging


object Boot extends App with LazyLogging {
  type OpPropsProvider = () ⇒ Props

  val clusterName = "ClusterSystem"
  implicit val system = ActorSystem(clusterName)

  sys.env.get("BOOT_ONE_AGENT") match {
    case None ⇒ startAll()
    case Some(name) ⇒
      val f: OpPropsProvider = name match {
        case "add" ⇒ Adder.props _
        case "sub" ⇒ Subtractor.props _
        case "mul" ⇒ Multiplier.props _
        case "div" ⇒ Divider.props _
        case _ ⇒
          logger.error("agent type was not set, use one of [add, sub, mul, div]")
          throw new RuntimeException("invalid agent type")
      }
      startOne(name, f)
  }

  def startOne(name: String, f: OpPropsProvider)(implicit system: ActorSystem) = {
    val cluster = Cluster(system)

    system.settings.config.getString("clustering.seeds").split(",").foreach { seed ⇒
      logger.info(s"$name is joining seed $seed as math")
      cluster.join(
        new Address("akka.tcp", clusterName, seed, 2551)
      )
    }

    val math = system.actorOf(f(), "math")
    ClusterClientReceptionist(system).registerService(math)
  }

  def startAll()(implicit system: ActorSystem) = {
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
}
