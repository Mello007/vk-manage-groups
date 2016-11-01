var app = angular.module('myApp',[]);

app.controller('adminCTRL', function ($scope, $http) {

    $scope.userWelcome = 'Приветствуем вас, незнакомец';
    $scope.loginned = false;
    $scope.groups = [];
    
    $scope.load = function (){
        $http.get('/user/getName').
        success(function(data) {
            $scope.response = data;
            $scope.userWelcome = 'Приветствуем вас, ' + data.userName + ' ' + data.userLastName + '!';
        });
    };

    $scope.loadGroups = function () {
        $http({
            method: 'GET',
            url: '/groups/get'
        }).then(function successCallback(data) {
           $scope.groups = data.data;
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