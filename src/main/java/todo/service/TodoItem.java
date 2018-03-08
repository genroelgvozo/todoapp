package todo.service;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "todoitems")
public class TodoItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "todo_id")
    private Integer id;

    @Column(name = "description")
    private String description;

    @Column(name = "done")
    private Boolean done;

    public TodoItem(String description){
        this.description = description;
        this.done = false;
    }

    TodoItem(){}

    public Integer getId() {
        return id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public Boolean getDone() {
        return done;
    }

    public void setDone(Boolean done) {
        this.done = done;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TodoItem todoItem = (TodoItem) o;
        return Objects.equals(id, todoItem.id) &&
                Objects.equals(description, todoItem.description) &&
                Objects.equals(done, todoItem.done);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, description, done);
    }

    @Override
    public String toString() {
        return "TodoItem{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", done=" + done +
                '}';
    }
}
