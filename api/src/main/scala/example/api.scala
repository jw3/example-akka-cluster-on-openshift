package example

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.{DefaultJsonProtocol, JsonFormat, RootJsonFormat}

object api {
  trait Operation {
    def lhs: Int
    def rhs: Int
  }

  trait OpResult {
    def res: Int
  }

  object OpResult extends DefaultJsonProtocol with SprayJsonSupport {
    implicit val format = new JsonFormat[OpResult] {
      import spray.json._

      val SumName = fname[Sum]
      val DiffName = fname[Difference]
      val ProdName = fname[Product]
      val QuotName = fname[Quotient]

      override def write(obj: OpResult): JsValue = obj match {
        case o: Sum ⇒ JsObject(SumName → o.toJson)
        case o: Difference ⇒ JsObject(DiffName → o.toJson)
        case o: Product ⇒ JsObject(ProdName → o.toJson)
        case o: Quotient ⇒ JsObject(QuotName → o.toJson)
      }

      override def read(json: JsValue): OpResult =
        json.asJsObject.fields.toSeq match {
          case Seq((SumName, js)) ⇒ js.convertTo[Sum]
          case Seq((DiffName, js)) ⇒ js.convertTo[Difference]
          case Seq((ProdName, js)) ⇒ js.convertTo[Product]
          case Seq((QuotName, js)) ⇒ js.convertTo[Quotient]
          case _ ⇒ throw new RuntimeException(s"unable to deserialize OpResult: $json")
        }
    }

    private def fname[T: Manifest]: String = manifest[T].runtimeClass.getSimpleName
  }


  case class Add(lhs: Int, rhs: Int) extends Operation
  case class Sum(res: Int) extends OpResult
  object Sum extends DefaultJsonProtocol {
    implicit val format: RootJsonFormat[Sum] = jsonFormat1(Sum.apply)
  }

  case class Subtract(lhs: Int, rhs: Int) extends Operation
  case class Difference(res: Int) extends OpResult
  object Difference extends DefaultJsonProtocol {
    implicit val format: RootJsonFormat[Difference] = jsonFormat1(Difference.apply)
  }

  case class Multiply(lhs: Int, rhs: Int) extends Operation
  case class Product(res: Int) extends OpResult
  object Product extends DefaultJsonProtocol {
    implicit val format: RootJsonFormat[Product] = jsonFormat1(Product.apply)
  }

  case class Divide(lhs: Int, rhs: Int) extends Operation
  case class Quotient(res: Int) extends OpResult
  object Quotient extends DefaultJsonProtocol {
    implicit val format: RootJsonFormat[Quotient] = jsonFormat1(Quotient.apply)
  }
}
