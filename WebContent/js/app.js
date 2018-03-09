(function () {
    var todolist = [];

    function htmlToElement(html) {
        var template = document.createElement('template');
        html = html.trim(); // Never return a text node of whitespace as the result
        template.innerHTML = html;
        return template.content.firstChild;
    }

    function renderItem(item) {

        var todoitem = $("<li class='todolist__item'><input type='checkbox' class='todolist__done_button'/><input type='text' class='todolist__description' value=''/><button class='todolist__delete'></button></li>");

        if (item.done){
            todoitem[0].classList.add("todolist__item_done");
            todoitem[0].firstChild.checked = true;
            todoitem[0].children[1].setAttribute("disabled","disabled");
        }
        todoitem[0].children[1].setAttribute("value",item["description"]);
        todoitem[0].setAttribute("id", item.id);

        todoitem[0].firstChild.addEventListener("click",toggleItem);
        todoitem[0].lastChild.addEventListener("click",deleteItem);
        todoitem[0].children[1].addEventListener("keypress",editTask)


        return todoitem;
    }

    function toggleItem() {
        $.ajax({
            url: "todo/toggle/"+$(this).parent().attr("id"),
            type: "PUT",
        }).done(function (r) {
            getAll();
        })
    }

    function deleteItem() {
        $.ajax({
            url: "todo/" + $(this).parent().attr("id"),
            type: "DELETE"
        }).done(function(r){
            getAll();
        })
    }

    function render() {
        var todo = document.querySelector(".todolist");
        while(todo.firstChild){
            todo.removeChild(todo.firstChild);
        }
        for (var index in todolist) {
            renderItem(todolist[index]).appendTo(".todolist");
        }
    }


    function getAll() {
        $.ajax({
            url: "todo/all",

            type: "GET",

            dataType: "json"
        })
            .done(function (json) {
                todolist = json;
                console.log(todolist);
                render();
            });
    }

    function addTask(event) {
        if(event.which == 13) {
            var desc = $(".form__input").val();
            $.ajax({
                url: "todo/add",

                data: desc,

                type: "POST",

                dataType: "json",
                contentType: "application/json",
                processData: false
            }).done(function (json) {
                getAll();
            }).fail(function (xhr, status, errorThrown) {
                console.log(status);
            });
        }
    }

    function editTask(event) {
        if(event.which == 13) {
            var desc = event.target.value;
            $.ajax({
                url: "todo/edit/"+$(event.target).parent().attr("id"),

                data: desc,

                type: "PUT",

                dataType: "json",
                contentType: "application/json",
                processData: false
            }).done(function (json) {
                getAll();
            }).fail(function (xhr, status, errorThrown) {
                console.log(status);
            });
        }
    }

    getAll();

    $(".form__input").keypress(addTask);


})();