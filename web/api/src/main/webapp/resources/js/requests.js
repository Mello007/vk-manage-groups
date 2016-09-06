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
}
function loadGroups() {
    var getGroupsInfo = new XMLHttpRequest();
    getGroupsInfo.open("GET", "user/getName", true);  //Указываем адрес GET-запроса
    getGroupsInfo.onload = function (){ //Функция которая отправляет запрос на сервер для получения имени пользователя
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
        data: requestJSONparametr,
        success: function (data) {
            alert("Преподаватель успешно удален!");
        },
        error: function (data) {
            alert("Не удалось удалить преподавателя! Возможно, вы ввели неправильно имя либо ввели недопустимое значение имени");
        }
    });
}

var globalvar = '';
$(document).ready(function() {
    $('.dropdown-menu-kind li a').click(function(){
        globalvar = $(this).data('val');
    })
});

function addNewStudent() {
    var name = $('#itemName').val();
    var requestJSONparametr = "{\"itemName\": \"" + name + "\", \"itemKind\": \"" + globalvar + "\"}";
    $.ajax({
        type: "POST",
        url: "/item/add",
        contentType: "application/json",
        dataType: 'json',
        data: requestJSONparametr,
        success: function (data) {
            alert("Предмет успешно добавлен!");
        },
        error: function (data) {
            alert("Не удалось добавить предмет! Что-то пошло не так, попробуйте еще раз");
        }
    });
}


// var x = new XMLHttpRequest();
// x.open("GET", "/student/all", true);  //Указываем адрес GET-запроса
// x.onload = function (){ //Функция которая отправляет запрос на сервер для получения всех студентов
//     var parsedItem = JSON.parse(this.responseText); //указываем что
//     var studentTable = document.getElementById('all-students'); //получаем данные на странице по Id  - all-student
//     parsedItem.forEach(function(item)  { //запускаем цикл
//         var fullNameElement = document.createElement('td'); //создаем элемент td для таблицы
//         fullNameElement.innerHTML =  item['fullName'] ; //внедряем имя студента из БД
//         var estimateElement = document.createElement('td');
//         estimateElement.innerHTML = item['itemPrice'];//создаем элемент td для таблицы
//         var elementContainer = document.createElement('tr'); //создаем тег
//         elementContainer.appendChild(fullNameElement);
//         elementContainer.appendChild(estimateElement);
//         studentTable.appendChild(elementContainer);
//     });
//     //подключаем к таблице библиотеку для сортировки
// };
// x.send(null);


// setInterval(x, 50000);


$(document).ready(function() {
    $('.dropdown-menu li a').click(function(){
        var val_cur = $(this).data('val');
        var requestJSONparametr = "{\"itemCurr\": \"" + val_cur + "\"}";
        $.ajax({
            type: "POST",
            url: "/item/curr",
            contentType: "application/json",
            dataType: 'json',
            data: requestJSONparametr,
            success: function (data) {
                alert("Цена установлена");
            },
            error: function (data) {
                alert("Не удалось установить цену!");
            }
        });
    });
});


var getStudent = new XMLHttpRequest();
getStudent.open("GET", "/student/all", true);  //Указываем адрес GET-запроса
getStudent.onload = function (){ //Функция которая отправляет запрос на сервер для получения всех студентов
    var parsedStudents = JSON.parse(this.responseText); //указываем что
    var studentsTable = document.getElementById('all-students'); //получаем данные на странице по Id  - all-student
    parsedStudents.forEach(function(item)  { //запускаем цикл
        var fullNameElement = document.createElement('td'); //создаем элемент td для таблицы
        fullNameElement.innerHTML =  item['fullName'] ; //внедряем имя студента из БД
        var estimateElement = document.createElement('td'); //создаем элемент td для таблицы
        var estimateList = item.estimate; //создаем объект который равент баллам за основные задания текущего студента
        var exestimateList = item.extensionEstimate; //создаем объект который равент баллам за доп задания текущего студента
        var estimateValue = 0; //создаем объект для того чтобы оценки складывались одна с одной
        estimateList.forEach(function(item, i, arr) {
            estimateValue+=item.estimate; //добавляем оценку студента
        });

        exestimateList.forEach(function(item, i, arr) {
            estimateValue+=item.estimate; //добавляем оценку студента
        });

        estimateElement.innerHTML =  estimateValue; //внедряем оценку в таблицу
        var respectElement = document.createElement('td'); //создаем тег td для таблицы
        var respectList = item['dateList']; //создаем объект который принимает dateList
        var respectValue = 0;
        var notRespectValue = 0;
        respectList.forEach(function(item, i, arr) {
            respectValue+=item.respectCause+item.notRespectCause; //складываем пропуски по уваж и по неуваж причинам
        });
        respectElement.innerHTML =  respectValue ; //внедряем пропуски
        var elementContainer = document.createElement('tr'); //создаем тег
        //добавляем все в тег
        elementContainer.appendChild(fullNameElement);
        elementContainer.appendChild(estimateElement);
        elementContainer.appendChild(respectElement);
        studentsTable.appendChild(elementContainer);
    });
    $("#all-student-table").tablesorter(); //подключаем к таблице библиотеку для сортировки
};

getStudent.send(null);