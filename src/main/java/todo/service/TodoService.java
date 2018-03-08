package todo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.inject.Named;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Optional;

@Component
@Named
@Path("/")
@Produces({MediaType.APPLICATION_JSON})
public class TodoService{

    @Context
    private HttpHeaders httpHeaders;

    @Autowired
    private TodoItemDAO todoItemDAO;

    @GET
    @Path("hello")
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return "hello";
    }


    @GET
    @Path("all")
    @Produces(MediaType.APPLICATION_JSON)
    public List<TodoItem> getAll() {
        return todoItemDAO.getAll();
    }

    @POST
    @Path("add")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public int createTodoItem(String description){
        TodoItem todoItem = new TodoItem(description);
        todoItemDAO.save(todoItem);
        return todoItem.getId();
    }

    @DELETE
    @Path("{todoItemId}")
    public void deleteTodoItem(@PathParam("todoItemId") String todoItemId) {
        todoItemDAO.delete(Integer.parseInt(todoItemId));
    }

    @PUT
    @Path("toggle/{todoItemId}")
    public void toggleTodoItem(@PathParam("todoItemId") String todoItemId) {
        Optional<TodoItem> todoItem = todoItemDAO.get(Integer.parseInt(todoItemId));
        if(todoItem.isPresent()) {
            todoItem.get().setDone(!todoItem.get().getDone());
            todoItemDAO.update(todoItem.get());
        }
    }

    @PUT
    @Path("edit/{todoItemId}")
    @Consumes(MediaType.APPLICATION_JSON)
    public void editTodoItem(@PathParam("todoItemId") String todoItemId, String description){
        Optional<TodoItem> todoItem = todoItemDAO.get(Integer.parseInt(todoItemId));
        if(todoItem.isPresent()) {
            todoItem.get().setDescription(description);
            todoItemDAO.update(todoItem.get());
        }
    }

}
