var pathToApplication = "/api-1.0-SNAPSHOT";

function getApi() {
    $.get("/oauth/vk", function (data) {
        window.open(data);
    });
}

function changeWelcome() {
    changeWelcome.try = changeWelcome.try || 10;
    changeWelcome.try = changeWelcome.try - 1;
    if (changeWelcome.try === 0) {
        return;
    }
    var getUserName = new XMLHttpRequest();
    getUserName.open("GET", pathToApplication + "/user/getName", true);  //Указываем адрес GET-запроса
    getUserName.onload = function () { //Функция которая отправляет запрос на сервер для получения имени пользователя
        if (this.responseText[0] === '<') {
            changeWelcome();
        }
        var parsedName = JSON.parse(this.responseText); //получаем объект их JSON ответа.
        document.getElementById('userName').innerHTML =
            parsedName.userName + ' ' + parsedName.userLastName;
    };
    getUserName.send(null);
    getUserName.onerror = changeWelcome;
}

function loadGroups() {
    //parameters of json object: name, avatar (there is address in vk), id group. That's all.
    var getGroupsInfo = new XMLHttpRequest();
    getGroupsInfo.open("GET", pathToApplication+ "/groups/get", true);  //Указываем адрес GET-запроса
    getGroupsInfo.onload = function () { //Функция которая отправляет запрос на сервер для получения имени пользователя
        var parsedName = JSON.parse(this.responseText), //получаем объект их JSON ответа.
            tableElement = document.getElementById('all-groups');
        tableElement.innerHTML = '';
        parsedName.forEach(function (group) {
            var tableRowElement = document.createElement('tr'),
                imgContainer = document.createElement('td'),
                nameContainer = document.createElement('td'),
                linkContainer = document.createElement('td');
                // linkContainer.createElement('span').className("hd");
            imgContainer.innerHTML = '<img src="' + group.photo50px + '" class = "img-circle" alt = "' + group.name +
                    '" width="50" height="50">';
            nameContainer.innerHTML = '<a href="vk.com/id' + group.groupId + '">' + group.groupName + '</a>';
            linkContainer.innerHTML = '<a href="#" class="btn btn-xs btn-embossed btn-primary" data-toggle="modal" data-target="#modal_default">Добавить бота</a>';
            tableRowElement.appendChild(imgContainer);
            tableRowElement.appendChild(nameContainer);
            tableRowElement.appendChild(linkContainer);
            tableElement.appendChild(tableRowElement);
        });
    };
    getGroupsInfo.send(null);
}


if (location.href.match(/admin/)) {
    setTimeout(changeWelcome, 1000);
    setTimeout(loadGroups, 10);
}

if (location.href.match(/manage-group/)) {
    changeWelcome();
}



function registerNewUser() {
    var email = $('#userEmail').val();
    var login = $('#login').val();
    var password = $('#password').val();
    var requestJSONparametr = "{\"userEmail\": \"" + email + "\", \"login\": \"" + login + "\", \"password\": \"" + password + "\"}";
    $.ajax({
        type: "POST",
        url: pathToApplication+"/user/new",
        contentType: "application/json",
        dataType: 'json',
        data: requestJSONparametr,
    });
}

//Вот такой json будет приходить на сервер

// [{"groupId":"116153191","groupName":"CSGO-LUCK.NET - Лучшая лотерея вещей!","photo50px":"https://pp.vk.me/c633821/v633821852/18d64/4J0IeTYxa9M.jpg","photo100px":"https://pp.vk.me/c633821/v633821852/18d63/UM2O1eooRs0.jpg","accessToken":null,"messagesOfGroup":null,"badMessages":null,"id":null},{"groupId":"41399075","groupName":"Lenkin.kz - официальная группа!","photo50px":"https://pp.vk.me/c307615/g41399075/e_1c8fef9b.jpg","photo100px":"https://pp.vk.me/c307615/g41399075/d_7fc9c462.jpg","accessToken":null,"messagesOfGroup":null,"badMessages":null,"id":null}]