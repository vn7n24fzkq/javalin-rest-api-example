/*
 * This Kotlin source file was generated by the Gradle 'init' task.
 */
package javalin.rest.api.example

import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.*
import io.javalin.http.Context

data class User(
    var id:Int,
    var name:String
)

fun main(args: Array<String>) {
    val app = Javalin.create().routes{
        path("users"){
            get(":id",UserController::getUserById)
            post(UserController::createUser)
            put(UserController::updateUser)
            delete(":id",UserController::removeUserById)
        }
    }.start(7000)
    app.get("/") { ctx -> ctx.result("Hello World") }
}


val users = HashMap<Int,User>()

object UserController{

    fun createUser(ctx:Context){
        ctx.bodyValidator<User>().get().apply{ 
            if(!users.containsKey(this.id))
                users.put(this.id,this)
            else
                ctx.status(400)
        }
    }
    
    fun getUserById(ctx:Context){
        val user = users.get(
            ctx.pathParam("id").toInt()
        )
        user?.let{
            ctx.json(it)
        }?:ctx.status(400)
    }

    fun removeUserById(ctx:Context){
        val id = ctx.pathParam("id").toInt()
        users.remove(id)
    }

    fun updateUser(ctx:Context){
        ctx.bodyValidator<User>().get().apply{ 
            if(users.containsKey(this.id))
                users.put(this.id,this)
            else
                ctx.status(400)
        }
    }

}

