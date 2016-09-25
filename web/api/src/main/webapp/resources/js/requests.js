function getApi() {
    $.get("/oauth/vk", function (data) {
        window.open(data);
    });
}

function changeWelcome() {
    var getUserName = new XMLHttpRequest();
    getUserName.open("GET", "/user/getName", true);  //Указываем адрес GET-запроса
    getUserName.onload = function () { //Функция которая отправляет запрос на сервер для получения имени пользователя
        if (this.responseText[0] === '<') {
            changeWelcome();
        }
        var parsedName = JSON.parse(this.responseText); //получаем объект их JSON ответа.
        document.getElementById('userName').innerHTML =
            'Приветствуем вас, ' + parsedName.userName + ' ' + parsedName.userLastName + '!';
    };
    getUserName.send(null);
    getUserName.onerror = changeWelcome;
}

function loadGroups() {
    //parameters of json object: name, avatar (there is address in vk), id group. That's all.
    var getGroupsInfo = new XMLHttpRequest();
    getGroupsInfo.open("GET", "group/get", true);  //Указываем адрес GET-запроса
    getGroupsInfo.onload = function () { //Функция которая отправляет запрос на сервер для получения имени пользователя
        var parsedName = JSON.parse(this.responseText), //получаем объект их JSON ответа.
            tableElement = document.getElementById('all-groups');
        tableElement.innerHTML = '';
        parsedName.forEach(function (group) {
            var tableRowElement = document.createElement('tr'),
                imgContainer = document.createElement('td'),
                nameContainer = document.createElement('td');
            imgContainer.innerHTML = '<img src="' + group.avatar + '" class = "img-circle" alt = "' + group.name +
                    '" width="50" height="50">';
            nameContainer.innerHTML = '<a href="' + group.id + '">' + group.name + '</a>';
            tableRowElement.appendChild(imgContainer);
            tableRowElement.appendChild(nameContainer);
            tableElement.appendChild(tableRowElement);
        });
    };
}




function login() {
    var email = $('#inputEmail3').val(),
        password = $('#inputPassword3').val(),
        requestJSONparametr = "{\"email\": \"" + email + "\", \"password\": \"" + password + "\"}";
    $.ajax({
        type: "POST",
        url: "/teacher/delete",
        contentType: "application/json",
        dataType: 'json',
        data: requestJSONparametr
    });
}

if (location.href.match(/admin/)) {
    setTimeout(changeWelcome, 0);
    setTimeout(loadGroups, 0);
}