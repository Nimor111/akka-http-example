package todo

import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.util.{Failure, Success}

import scala.io.StdIn

import akka.http.scaladsl.Http
import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._

object TodoServer extends App with TodoRoutes {
  implicit val actorSystem: ActorSystem = ActorSystem("todo-app")
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  implicit val executionContext = actorSystem.dispatcher

  val todoActor = actorSystem.actorOf(TodoActor.props, "todo-actor")

  val binding = Http().bindAndHandle(todoRoutes, "localhost", 8081)

  println("Server started on port 8080...")

  binding.onComplete {
    case Success(bound) =>
      println(s"Server online at http://${bound.localAddress.getHostString}:${bound.localAddress.getPort}/")
    case Failure(e) => actorSystem.terminate()
  }

  // Press ENTER to exit
  StdIn.readLine()

  binding.flatMap(_.unbind()).onComplete(_ => actorSystem.terminate())
}
