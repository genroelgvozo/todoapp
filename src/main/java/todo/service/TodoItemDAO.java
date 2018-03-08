package todo.service;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static java.util.Objects.requireNonNull;

@Repository("todoItemDAO")
public class TodoItemDAO {

    @Autowired
    private SessionFactory sessionFactory;

    public TodoItemDAO(SessionFactory sessionFactory){
        this.sessionFactory = requireNonNull(sessionFactory);
    }

    @Transactional
    public void save(TodoItem todoItem){
        if(todoItem.getId() != null) {
            throw new IllegalArgumentException("Cannot save item with assigned id");
        }

        session().save(todoItem);
    }

    @Transactional
    public Optional<TodoItem> get(int todoItemId) {
        TodoItem todoItem = (TodoItem) session().get(TodoItem.class, todoItemId);

        return Optional.ofNullable(todoItem);
    }

    @Transactional
    public List<TodoItem> getAll() {
        Criteria criteria = session().createCriteria(TodoItem.class);

        List<TodoItem> todos = criteria.list();

        return todos;
    }

    @Transactional
    public void update(TodoItem todoItem){
        session().update(todoItem);
    }

    @Transactional
    public void delete(int todoItemId){
        session().createQuery("DELETE TodoItem WHERE id = :id")
                .setInteger("id",todoItemId)
                .executeUpdate();
    }

    private Session session(){
        return sessionFactory.getCurrentSession();
    }
}
