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

            if($scope.parent){
                formData.append('pp', $scope.parent.idPost);
            }
            if($scope.idlist){
              formData.append("l", $scope.idlist);
            }
            formData.append("t", $scope.txtPost);
            userService.post(formData)
        }
        $scope.habilitaImagem = function(e){
            $scope.files = [];
            $scope.addImagem = true;
        }
    }],
    scope: {
        parent: '=',
        idlist: '='
    },
    templateUrl: 'templates/post-area.html'
  };
}



function mainArea(){
	return {
	  controller: ['$scope', '$mdSidenav', '$timeout', 'userService', function($scope, $mdSidenav, $timeout, userService){
            $scope.openMenu = function(){
                console.log("close");
                $timeout(function() { $mdSidenav('left').open(); });
            }
            $scope.user = userService.getLoggedUser().userEd;
            console.log($scope.user);
            $scope.listas = [];
            userService.getListsFollowed(function(listas){
                $scope.listas = listas;
            });

            $scope.seguindo = [];
            $scope.seguidores = [];
            userService.getFollowingAndFollowers(function(follow){
                $scope.seguindo = follow.following;
                $scope.seguidores = follow.followers;
            });

      }],
	  transclude: true,
      templateUrl: 'templates/main-area.html'
  };
}

function timeLine(){
	return {
	    controller: ['$scope', '$timeout', function($scope, $timeout){
            $scope.posts =[];
            $scope.carregando = true;
            $scope.carregarmais = function(loadMore){
                $scope.carregando = true;
                var data = {};
                var firstPost = null;
                console.log(loadMore)
                if(loadMore){
                    var lastPost = $scope.posts[$scope.posts.length - 1].idPost;
                    data.lp = lastPost;
                }


                $scope.fnextpage(data, angular.bind(this, function(posts){
                        $scope.carregando = false;
                        $scope.posts = $scope.posts.concat(posts);
                  }));
            };

            $timeout($scope.carregarmais);
/*
            $scope.infiniteItems = {
                  numLoaded_: 0,
                  toLoad_: 0,
                  posts: [],
                  // Required.
                  getItemAtIndex: function(index) {
                    if (index > this.numLoaded_) {
                      this.fetchMoreItems_(index);
                      return null;
                    }
                    return this.posts[index];
                  },
                  // Required.
                  // For infinite scroll behavior, we always return a slightly higher
                  // number than the previously loaded items.
                  getLength: function() {
                    return this.numLoaded_ + 5;
                  },
                  fetchMoreItems_: function(index) {
                    // For demo purposes, we simulate loading more items with a timed
                    // promise. In real code, this function would likely contain an
                    // $http request.
                    if (this.toLoad_ < index) {
                      this.toLoad_ += 5;
                      console.log("carregando");
                      $scope.fnextpage(angular.bind(this, function(posts){
                            console.log(posts);
                            this.posts = this.posts.concat(posts);
                            this.numLoaded_ = this.toLoad_;
                      }));
                    }
                  }
                };
<md-virtual-repeat-container id="vertical-container" layout-fill>
    <div md-virtual-repeat="post in infiniteItems" md-on-demand class="repeated-item" flex>
        <div time-line-item post="post"></div>
    </div>
</md-virtual-repeat-container>
*/

        }],
        scope: {
          fnextpage: '='
        },
        templateUrl: 'templates/time-line.html'
    };
}


function timeLineItem(){
	return {
	    controller: ['$scope', '$timeout', 'userService', function($scope, $timeout, userService){
	        $scope.respondendo = false;
	        $scope.isMine = false;
            $scope.iLiked = false;

            var carregapost = function(){
                $scope.isMine = userService.getLoggedUser().userEd.idUsuario == $scope.post.idUser;
                $scope.iLiked = false;
                for (var i = 0; i < $scope.post.likes.length; i++) {
                    if($scope.post.likes[i].idUser == userService.getLoggedUser().userEd.idUsuario){
                        $scope.iLiked = true;
                        break;
                    }

                }
            }

	        $timeout(carregapost);

	        $scope.iniciarresposta = function (){
	            $scope.respondendo = true;
	        }

            $scope.deleta = function(){
                userService.deletePost($scope.post, function(){
                    console.log("deletado");
                });
            }



            $scope.curtir = function(){
                console.log ($scope.iLiked);
                if ($scope.iLiked){
                    userService.dislike($scope.post, function(post){
                        $scope.post = post;
                        carregapost();
                    });
                } else {
                    userService.like($scope.post, function(post){
                        $scope.post = post;
                        carregapost();
                    });
                }

            }





	    }],
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