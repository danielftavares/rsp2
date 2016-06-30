function RspCtrl($scope, $rootScope, $location, userMsgService ){

    $rootScope.$on( 'rspHttpError', function( event, response ) {
        console.log($location);
        console.log(response);

        if(response.status == 401){
            if($location.path() == '/login'){
                userMsgService.showToast("Senha incorreta!");
            } else {
                userMsgService.showToast("Login expirador!");
            }
        } else {
            userMsgService.showToast( "Erro!" );
        }
    })
}

function LoginCtrl ($scope, $location, userService, userDataService){

    if (userDataService.isUserLogged()){
        $location.path( '/main' );
    }

	$scope.login = {name: '', password: ''}

	$scope.doLogin = function(){
		userService.login($scope.login.name, $scope.login.password,
		function(loggedUser){
		   $location.path( '/main' );
		})
	}
}

function MainCtrl ($scope, userService){
    $scope.listPosts = function(data, callback){
        userService.listPosts(data, callback);
    }

}

function UserCtrl ($scope, $routeParams, userService){

    $scope.listPosts = function(data, callback){
        data.u = $routeParams.iduser;
        userService.listPosts(data, callback);
    }

    userService.findUserById($routeParams.iduser, function(user){
        $scope.user = user;
    });

    userService.getFieldsValue($routeParams.iduser, function(fields){
        $scope.profileValues = fields;
    });

}

function UserEditCtrl($scope, userService, userDataService) {
    $scope.mapProfileValue = {};

    userService.findUserById(userDataService.getLoggedUser().userEd.idUsuario, function(user){
        $scope.user = user;
        $scope.user.files  = [];
    });

    userService.getProfileFields(function (fields){
        $scope.fields = fields;
        userService.getFieldsValue(userDataService.getLoggedUser().userEd.idUsuario, function(userFields){
            $scope.userFields = userFields;
            angular.forEach(userFields, function(userfield){
                $scope.mapProfileValue[userfield.profileField.idProfileField] = userfield.value;
            });
        });
    });

    $scope.btnSalvarClick = function(){

        var formData = new FormData();
        formData.append("nome", $scope.user.nome);
        angular.forEach($scope.mapProfileValue, function(value, key){
            formData.append("f"+key, value);
        });

        console.log($scope.user.files);
        angular.forEach($scope.user.files,function(obj){
            console.log(obj);
            formData.append('pi', obj.lfFile);
        });

        userService.updateProfile(formData, function(){
            console.log("salvou!!!")
        });
    }
}

function ListNewCtrl($scope, userService) {
    $scope.lista = {nome: ''};
    $scope.btnSalvarClick = function(){
        console.log($scope.lista.nome);
        userService.insertList($scope.lista.nome, function(){
            console.log("salvou!!!")
        });
    }
}


function ListCtrl ($scope, $routeParams, userService){

    $scope.listPosts = function(data, callback){
        data.l = $routeParams.idList;
        userService.listPosts(data, callback);
    }

    userService.findListById($routeParams.idList, function(list){
        $scope.list = list;
    });

}


angular.module('rspApp')
    .controller('RspCtrl', ['$scope', '$rootScope', '$location', 'userMsgService', RspCtrl])
	.controller('LoginCtrl', ['$scope', '$location', 'userService', 'userDataService', LoginCtrl])
	.controller('MainCtrl', ['$scope', 'userService', MainCtrl])
	.controller('UserCtrl', ['$scope', '$routeParams', 'userService', UserCtrl])
	.controller('UserEditCtrl', ['$scope', 'userService', 'userDataService', UserEditCtrl])
	.controller('ListNewCtrl', ['$scope', 'userService', ListNewCtrl])
	.controller('ListCtrl', ['$scope', '$routeParams', 'userService', ListCtrl]);
