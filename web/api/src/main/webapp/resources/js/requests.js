// function getApi() {
//     $.get("oauth/vk", function (data) {
//         window.open(data);
//     });
// }

function changeWelcome() {
    var getUserName = new XMLHttpRequest();
    getUserName.open("GET", "user/getName", true);  //Указываем адрес GET-запроса
    getUserName.onload = function (){ //Функция которая отправляет запрос на сервер для получения имени пользователя
        var parsedName = JSON.parse(this.responseText); //получаем объект их JSON ответа.
        document.getElementById('userName').innerHTML =
            'Приветствуем вас, ' + parsedName.userName + ' ' + parsedName.userLastName + '!';
    };
    getUserName.send(null);
}
function loadGroups() {
    var getGroupsInfo = new XMLHttpRequest();
    getGroupsInfo.open("GET", "user/GetGroups", true);  //Указываем адрес GET-запроса
    getGroupsInfo.onload = function (){ //Функция которая отправляет запрос на сервер для получения имени пользователя
        var parsedGroupsInfo = JSON.parse(this.responseText); //получаем объект их JSON ответа.
        var groupTable = document.getElementById('all-students');
        for (var i = 0; i < parsedGroupsInfo.length; i++) {
            var tableRowElement = document.createElement('tr');
            //groupName, groupAvatar
            var groupAvatarElement = document.createElement('td');
            groupAvatarElement.style.background = 'url(' + parsedGroupsInfo.groupAvatar + ')';
            groupAvatarElement.width = '100px';
            groupAvatarElement.height = '100px';
            tableRowElement.appendChild(groupAvatarElement);

            var groupNameElement = document.createElement('td');
            groupNameElement.innerHTML = parsedGroupsInfo.groupName;
            tableRowElement.appendChild(groupNameElement);

            var managementElement = document.createElement('td');
            var managementLinkElement = document.createElement('a');
            managementLinkElement.innerHTML = 'Управлять группой';
            managementLinkElement.href = "manage-groups.html";
            managementElement.appendChild(managementLinkElement);
        }
    };
    getGroupsInfo.send(null);
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
        data: requestJSONparametr,
        success: function (data) {
            alert("Преподаватель успешно удален!");
        },
        error: function (data) {
            alert("Не удалось удалить преподавателя! Возможно, вы ввели неправильно имя либо ввели недопустимое значение имени");
        }
    });
}
