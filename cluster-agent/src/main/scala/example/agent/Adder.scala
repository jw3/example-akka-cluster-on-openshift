package example.agent

import akka.actor.{Actor, ActorLogging, Props}
import example.agent.Adder.{Add, Sum}

object Adder {
  def props() = Props(new Adder)

  case class Add(lhs: Int, rhs: Int)
  case class Sum(sum: Int)
}

class Adder extends Actor with ActorLogging {
  def receive = {
    case Add(l, r) ⇒ sender ! Sum(l + r)
    case s: String ⇒ println(s"received string message ===> $s")
  }
}
