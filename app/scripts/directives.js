function postArea(){
	return {
    controller: ['$scope', '$timeout', 'userService',  function($scope, $timeout, userService) {
    	$scope.editing = false;
    	$scope.addImagem = false;
    	$scope.files = [];
        $scope.focusPostArea = function(){
            $scope.editing = true;
        }
        $scope.focusLostPostArea = function(e){
            $timeout(function(){
                if(!$scope.addImagem && !$scope.txtPost){
                    $scope.editing = false;
                }
            }, 1000);
        }
        $scope.postar = function(e){
            console.log($scope);
            var formData = new FormData();
            angular.forEach($scope.files,function(obj){
                console.log(obj);
                formData.append('pi', obj.lfFile);
            });
            formData.append("t", $scope.txtPost);
            userService.post(formData)
        }
        $scope.habilitaImagem = function(e){
            $scope.files = [];
            $scope.addImagem = true;
        }
    }],
    templateUrl: 'templates/post-area.html'
  };
}



function mainArea(){
	return {
	  controller: ['$scope', '$mdSidenav', '$timeout', function($scope, $mdSidenav, $timeout){
            $scope.openMenu = function(){
                console.log("close");
                $timeout(function() { $mdSidenav('left').open(); });
            }
      }],
	  transclude: true,
      templateUrl: 'templates/main-area.html'
  };
}

function timeLine(){
	return {
        scope: {
          posts: '='
        },
        templateUrl: 'templates/time-line.html'
    };
}


function timeLineItem(){
	return {
        templateUrl: 'templates/time-line-item.html',
        scope: {
          post: '='
        }
    };
}


function followBtn(userService){
	return {
        controller: ['$scope', '$location', function($scope, $location){

                    var fLoadStatus = function (user){
                            if(userService.isMe(user)){
                                $scope.acao = "Editar perfil";
                                $scope.btnFollowClick = fEditarPerfil;
                            } else {
                                userService.amIFollowing(user.idUsuario, function (r){
                                                                                if(r) {
                                                                                    $scope.acao = "Deixar de Seguir";
                                                                                    $scope.btnFollowClick = fUnfollow;
                                                                                } else {
                                                                                    $scope.acao = "Seguir";
                                                                                    $scope.btnFollowClick = fFollow;
                                                                                }
                                                                            });
                            }
                    }

                    var fEditarPerfil = function(u){
                        $location.path( '/editprofile' );
                        console.log("entrou em editar perfil");
                    }

                    var fFollow = function(u){
                        userService.follow(u, function(){
                            fLoadStatus(u);
                        });
                    }
                    var fUnfollow = function(u){
                        userService.unfollow(u, function(){
                            fLoadStatus(u);
                        });
                    }

                    $scope.btnFollowClick = function(u){
                        console.log("Carregando")
                    }

                    $scope.acao = "Seguir";

                    $scope.$watch('user', function (user) {
                                        if (user) {
                                            fLoadStatus(user);
                                        }
                                        });

              }],
        scope: {
          user: '='
        },
        template: '<md-button ng-click="btnFollowClick(user)" class="md-raised md-primary">{{acao}}</md-button>'
    };
}

angular.module('rspApp')
	.directive('postArea', postArea)
	.directive('mainArea', mainArea)
	.directive('timeLine', timeLine)
	.directive('timeLineItem', timeLineItem)
	.directive('followBtn', ['userService', followBtn])
	;