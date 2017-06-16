package example.agent

import akka.actor.{Actor, ActorLogging, Props}
import example.agent.Subtractor.{Difference, Subtract}
import spray.json.{DefaultJsonProtocol, RootJsonFormat}

object Subtractor {
  def props() = Props(new Adder)

  case class Subtract(lhs: Int, rhs: Int) extends Operation
  case class Difference(res: Int) extends OpResult
  object Difference extends DefaultJsonProtocol {
    implicit val format: RootJsonFormat[Difference] = jsonFormat1(Difference.apply)
  }
}

class Subtractor extends Actor with ActorLogging {
  def receive = {
    case Subtract(l, r) ⇒ sender ! Difference(l - r)
  }
}
