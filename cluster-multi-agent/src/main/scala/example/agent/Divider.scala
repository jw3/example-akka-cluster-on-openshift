package example.agent

import akka.actor.{Actor, ActorLogging, Props}
import example.agent.Divider.{Divide, Quotient}
import spray.json.{DefaultJsonProtocol, RootJsonFormat}


object Divider {
  def props() = Props(new Adder)

  case class Divide(lhs: Int, rhs: Int) extends Operation
  case class Quotient(res: Int) extends OpResult
  object Quotient extends DefaultJsonProtocol {
    implicit val format: RootJsonFormat[Quotient] = jsonFormat1(Quotient.apply)
  }
}

class Divider extends Actor with ActorLogging {
  def receive = {
    case Divide(l, r) ⇒ sender ! Quotient(l / r)
  }
}
