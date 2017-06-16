package example.agent

import akka.actor.{Actor, ActorLogging, Props}
import example.agent.Multiplier.{Multiply, Product}
import spray.json.{DefaultJsonProtocol, RootJsonFormat}

object Multiplier {
  def props() = Props(new Adder)

  case class Multiply(lhs: Int, rhs: Int) extends Operation
  case class Product(res: Int) extends OpResult
  object Product extends DefaultJsonProtocol {
    implicit val format: RootJsonFormat[Product] = jsonFormat1(Product.apply)
  }
}

class Multiplier extends Actor with ActorLogging {
  def receive = {
    case Multiply(l, r) ⇒ sender ! Product(l * r)
  }
}
