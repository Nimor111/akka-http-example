package todo

import scala.concurrent.Future
import scala.concurrent.duration._

import akka.actor.ActorSystem
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.pattern.ask
import akka.util.Timeout
import akka.stream.ActorMaterializer
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.http.scaladsl.marshalling.Marshal
import scala.concurrent.ExecutionContext
import akka.actor.ActorRef

trait TodoRoutes extends JsonSupport {
  implicit val actorSystem: ActorSystem
  implicit val timeout: Timeout = 5.seconds

  def todoActor: ActorRef

  lazy val todoRoutes = {
    pathPrefix("v1" / "todos") {
      get {
        path(Segment) { todoId =>
          val todo: Future[Option[Todo]] = (todoActor ? TodoActor.GetTodoByID(todoId)).mapTo[Option[Todo]]
          complete {
            todo
          }
        }
      } ~
      get {
        val todos: Future[Seq[Todo]] = (todoActor ? TodoActor.GetTodos).mapTo[Seq[Todo]]
        complete {
          todos
        }
      } ~
      post {
        entity(as[Todo]) { todo =>
          val newTodo: Future[Todo] = (todoActor ? TodoActor.CreateTodo(todo)).mapTo[Todo]
          complete {
            StatusCodes.Created -> newTodo
          }
        }
      } ~
      delete {
        path(Segment) { todoID => {
          todoActor ! TodoActor.DeleteTodoByID(todoID)
            complete {
              StatusCodes.NoContent
            }
          }
        }
      }
    }
  }
}
