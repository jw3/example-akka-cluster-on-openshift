package example.agent

import akka.actor.{Actor, ActorLogging, Props}
import example.agent.Adder.{Add, Sum}
import spray.json.{DefaultJsonProtocol, RootJsonFormat}

object Adder {
  def props() = Props(new Adder)

  case class Add(lhs: Int, rhs: Int) extends Operation
  case class Sum(res: Int) extends OpResult
  object Sum extends DefaultJsonProtocol {
    implicit val format: RootJsonFormat[Sum] = jsonFormat1(Sum.apply)
  }
}

class Adder extends Actor with ActorLogging {
  def receive = {
    case Add(l, r) ⇒ sender ! Sum(l + r)
  }
}
