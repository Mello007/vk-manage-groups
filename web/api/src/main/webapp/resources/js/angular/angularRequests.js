var app = angular.module('myApp',[]);

app.controller('adminCTRL', function ($scope, $http) {

    $scope.userWelcome = 'Приветствуем вас, незнакомец';
    $scope.loginned = false;
    $scope.groups = [];
    
    $scope.groups = [
        {   "groupId":"41399075",
            "groupName":"Lenkin.kz - официальная группа!",
            "photo50px":"https://pp.vk.me/c307615/g41399075/e_1c8fef9b.jpg",
            "photo100px":"https://pp.vk.me/c307615/g41399075/d_7fc9c462.jpg",
            "accessToken":null,
            "tempKeyOfPollingServer":null,
            "addressOfPollingServer":null,
            "numberOfLastAction":null,
            "question":null,
            "badMessage":null,
            "answer":null,
            "stopWords":null,
            "messagesOfGroup":null,
            "answerAndAsksMessages":null,
            "id":113
        },
        {   "groupId":"116153191",
            "groupName":"CSGO-LUCK.NET - Лучшая лотерея вещей!",
            "photo50px":"https://pp.vk.me/c633821/v633821852/18d64/4J0IeTYxa9M..",
            "photo100px":"https://pp.vk.me/c633821/v633821852/18d63/UM2O1eooRs0..",
            "accessToken":null,
            "tempKeyOfPollingServer":null,
            "addressOfPollingServer":null,
            "numberOfLastAction":null,
            "question":null,
            "badMessage":null,
            "answer":null,
            "stopWords":null,
            "messagesOfGroup":null,
            "answerAndAsksMessages":null,
            "id":114
        }
    ];

    $scope.load = function (){
        $http.get('/user/getName').
        success(function(data) {
            $scope.userWelcome = 'Приветствуем вас, ' + data.userName + ' ' + data.userLastName + '!';
        });
        $http({
            method: 'GET',
            url: '/user/getName'
        }).then(function successCallback(data) {
            $scope.userWelcome = 'Приветствуем вас, ' + data.userName + ' ' + data.userLastName + '!';
            $scope.loginned = true;
        }, function errorCallback(response) {
            alert('login error!');
            //обработка ошибки. но это же нам не надо, ведь теперь все в норме, да, брат?
        });
    };

    $scope.loadGroups = function () {
        $http({
            method: 'GET',
            url: '/groups/get'
        }).then(function successCallback(data) {
           $scope.groups = data;
        }, function errorCallback(response) {
            //обработка ошибки. но это же нам не надо, ведь теперь все в норме, да, брат?
            alert('load groups error!');
        });
    };
    $scope.newUser = function () {
        //var email = $('#userEmail').val();
        //var login = $('#login').val();
        //var password = $('#password').val();
        
        $http.post('/register/new', {
            userEmail: $scope.email,
            login: $scope.login,
            password: $scope.password
        }).then(function () {
            alert("Пользователь успешно добавлен!");
        }, function () {
            alert("Не удалось добавить пользователя! Что-то пошло не так, попробуйте еще раз");
        });
    };
    $scope.init = function () {
        $scope.load();
        $scope.loadGroups();
    }
});
//Вот такой json будет приходить на сервер

// [{"groupId":"116153191","groupName":"CSGO-LUCK.NET - Лучшая лотерея вещей!","photo50px":"https://pp.vk.me/c633821/v633821852/18d64/4J0IeTYxa9M.jpg","photo100px":"https://pp.vk.me/c633821/v633821852/18d63/UM2O1eooRs0.jpg","accessToken":null,"messagesOfGroup":null,"badMessages":null,"id":null},{"groupId":"41399075","groupName":"Lenkin.kz - официальная группа!","photo50px":"https://pp.vk.me/c307615/g41399075/e_1c8fef9b.jpg","photo100px":"https://pp.vk.me/c307615/g41399075/d_7fc9c462.jpg","accessToken":null,"messagesOfGroup":null,"badMessages":null,"id":null}]