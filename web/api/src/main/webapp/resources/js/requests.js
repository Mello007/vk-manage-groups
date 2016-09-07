function getApi() {
    $.get("/oauth/vk", function (data) {
        window.open(data);
    });
}

function changeWelcome() {
    var getUserName = new XMLHttpRequest();
    getUserName.open("GET", "user/getName", true);  //Указываем адрес GET-запроса
    getUserName.onload = function () { //Функция которая отправляет запрос на сервер для получения имени пользователя
        var parsedName = JSON.parse(this.responseText); //получаем объект их JSON ответа.
        document.getElementById('userName').innerHTML =
            'Приветствуем вас, ' + parsedName.userName + ' ' + parsedName.userLastName + '!';
    };
    getUserName.send(null);
}

if (location.href.match(/admin/)) {
    changeWelcome();
}

function loadGroups() {
    var getGroupsInfo = new XMLHttpRequest();
    getGroupsInfo.open("GET", "user/getName", true);  //Указываем адрес GET-запроса
    getGroupsInfo.onload = function () { //Функция которая отправляет запрос на сервер для получения имени пользователя
        var parsedName = JSON.parse(this.responseText); //получаем объект их JSON ответа.
        document.getElementById('userName').innerHTML =
            'Приветствуем вас, ' + parsedName.userName + ' ' + parsedName.userLastName + '!';
    };
}

function login() {
    var email = $('#inputEmail3').val();
    var password = $('#inputPassword3').val();
    var requestJSONparametr = "{\"email\": \"" + email + "\", \"password\": \"" + password + "\"}";
    $.ajax({
        type: "POST",
        url: "/teacher/delete",
        contentType: "application/json",
        dataType: 'json',
        data: requestJSONparametr
    });
}