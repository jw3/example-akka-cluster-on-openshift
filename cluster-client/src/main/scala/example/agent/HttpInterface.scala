package example.agent

import akka.actor.{ActorRef, ActorSystem}
import akka.cluster.client.ClusterClient
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{StatusCode, StatusCodes}
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import akka.util.Timeout
import example.api._

import scala.concurrent.ExecutionContext


object HttpInterface extends HttpInterface {
  def up(ref: ActorRef, host: String, port: Int)(implicit system: ActorSystem, mat: ActorMaterializer, timeout: Timeout) = {
    import system.dispatcher
    Http().bindAndHandle(routes(ref), host, port)
  }
}

trait HttpInterface {
  import akka.http.scaladsl.server.Directives._
  import akka.pattern.ask


  def routes(c: ActorRef)(implicit ec: ExecutionContext, timeout: Timeout): Route = {
    pathPrefix("api") {
      path("op" / IntNumber / Segment / IntNumber) { (lhs, op, rhs) ⇒
        val f: (Int, Int) ⇒ Operation = op match {
          case "add" ⇒ Add.apply
          case "sub" ⇒ Subtract.apply
          case "mul" ⇒ Multiply.apply
          case "div" ⇒ Divide.apply
        }

        val r = c ? ClusterClient.Send(s"/user/$op", f(lhs, rhs), localAffinity = true)
        complete(r.map {
          case ec: StatusCode ⇒ ec
          case r: OpResult ⇒ StatusCodes.NotFound
        })
      }
    }
  }
}
