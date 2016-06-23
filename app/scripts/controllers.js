function LoginCtrl ($scope, $location, userService){

    if (userService.isUserLogged()){
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

function UserEditCtrl($scope, userService) {
    $scope.mapProfileValue = {};
    userService.findUserById(userService.getLoggedUser().userEd.idUsuario, function(user){
        $scope.user = user;
    });

    userService.getProfileFields(function (fields){
        $scope.fields = fields;
        userService.getFieldsValue(userService.getLoggedUser().userEd.idUsuario, function(userFields){
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
        userService.updateProfile(formData, function(){
            console.log("salvou!!!")
        });
    }
}


angular.module('rspApp')
	.controller('LoginCtrl', ['$scope', '$location', 'userService', LoginCtrl])
	.controller('MainCtrl', ['$scope', 'userService', MainCtrl])
	.controller('UserCtrl', ['$scope', '$routeParams', 'userService', UserCtrl])
	.controller('UserEditCtrl', ['$scope', 'userService', UserEditCtrl]);
