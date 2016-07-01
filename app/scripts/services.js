function UserService($http, userDataService, $httpParamSerializer){
    var userService = this;

    userService.login = function(username, password, callback){
        $http(
            {
                method  : 'POST',
                url: "apiv1/user/login",
                data    : {"username": username, "password": password }
            }
        ).then(function successLogin(response) {
            if (response){
                userDataService.setUserLogged(response.data);
                callback(response.data);
            }
        }, function errorLogin(response) {

        });
    };

    userService.listPosts = function(info, callback){
        $http(
            {
                method  : 'GET',
                url: 'apiv1/post',
                params: info,
                responseType: "json"
            }
        ).then(function success(response) {
            callback(response.data);
         }, function error(response) {
             // TODO
         });
    };

    userService.post = function(formData, callback){
        $http.post('apiv1/post', formData, {
            headers: {
              'Content-Type': undefined
            },
            transformRequest: angular.identity
        }).then(function(result){
            // do sometingh
        },function(err){
            // do sometingh
        });
    }

    userService.findUserById = function(idUser, callback){
            $http(
                {
                    method  : 'GET',
                    url: 'apiv1/user/'+idUser,
                    responseType: "json"
                }
            ).then(function success(response) {
                         callback(response.data);
             }, function error(response) {
                 // TODO
             });
    }

    userService.getFieldsValue = function(iduser, callback){
        $http(
            {
                method  : 'GET',
                url: 'apiv1/profile/'+iduser,
                responseType: "json"
            }
        ).then(function success(response) {
                     callback(response.data);
         }, function error(response) {
             // TODO
         });
    }


    userService.follow = function(user,callback){
        $http(
            {
                method  : 'GET',
                url: 'apiv1/user/f/'+user.idUsuario,
                method: 'GET'
            } ).then(function success(response) {
                userService.following = [];
                callback(response.data);
            }, function error(response) {
                // TODO
            });
    }

    userService.unfollow = function(user,callback){
       $http(
            {
                method  : 'GET',
                url: 'apiv1/user/uf/'+user.idUsuario,
                method: 'GET'
            } ).then(function success(response) {
                userService.following = [];
                callback(response.data);
            }, function error(response) {
                // TODO
            });
    }

    userService.following = [];

    userService.getFollowingAndFollowers = function(callback){
        userService.listFollowingAndFollowers(userDataService.getLoggedUser().userEd.idUsuario, function(l){ userService.following = l.following; callback(l) }, this);
    }

    userService.listFollowingAndFollowers = function(idUser, callback){
        $http(
            {
                method  : 'GET',
                url: 'apiv1/user/lf/'+idUser,
                responseType: 'json'
            }
           ).then(function success(response) {
                       callback(response.data);
           }, function error(response) {
               // TODO
           });
      }

    userService.amIFollowing = function(idUser, callbackf){
        if(userService.following.length == 0){
          userService.listFollowingAndFollowers(userDataService.getLoggedUser().userEd.idUsuario,
            function(l){
                userService.following = l.following;
                userService._amIFollowing(idUser, callbackf)
            });
        } else {
          userService._amIFollowing(idUser, callbackf);
        }
    }

    userService._amIFollowing = function(idUser, callback){
        var f = false;
        for (var i = this.following.length - 1; i >= 0; i--) {
          var u = this.following[i];
          if(u.idUsuario == idUser){
            f = true;
          }
        }
        callback(f);
    }

    userService.fields = [];
    userService.getProfileFields = function(callback){

    	if(userService.fields.length == 0){
    	        $http(
                    {
                        method  : 'GET',
                        url: 'apiv1/profile/f',
                        responseType: 'json'
                    }
                   ).then(function success(response) {
                       userService.fields = response.data;
                       callback(response.data);
                   }, function error(response) {
                       // TODO
                   });
    	} else {
    		callback(userService.fields);
    	}
    }

    userService.updateProfile = function(profileForm, callback){

            $http.post('apiv1/user/profile', profileForm, {
                headers: {
                  'Content-Type': undefined
                },
                transformRequest: angular.identity
            }).then(function(result){
                callback();
            },function(err){
                // do sometingh
            });


    }



    userService.like = function(post, callback) {

        $http.post('apiv1/post/l/'+post.idPost, null, {
                      headers: {
                        'Content-Type': undefined
                      },
                      transformRequest: angular.identity
                  }).then(function(result){
                      callback(result.data);
                  },function(err){
                      // do sometingh
                  });
    }

    userService.dislike = function(post, callback) {
          $http.post('apiv1/post/dl/'+post.idPost, null, {
                        headers: {
                          'Content-Type': undefined
                        },
                        transformRequest: angular.identity
                    }).then(function(result){
                        callback(result.data);
                    },function(err){
                        // do sometingh
                    });
    }

    userService.deletePost = function(post, callback) {
            $http.post('apiv1/post/d/'+post.idPost, null, {
                          headers: {
                            'Content-Type': undefined
                          },
                          transformRequest: angular.identity
                      }).then(function(result){
                          callback(result.data);
                      },function(err){
                          // do sometingh
                      });
    }


    userService.insertList = function(listname, callback) {
                $http.post('apiv1/list', $httpParamSerializer({ln: listname }), {
                              headers: {
                                "Content-Type": "application/x-www-form-urlencoded;"
                              },
                              responseType: "json"
                          }).then(function(result){
                              callback(result.data);
                          },function(err){
                              // do sometingh
                          });
    }

    userService.listfollowing = [];

    userService.amIFollowingList = function(idList, returncallback){
        if(userService.listfollowing.length == 0){
          userService.getListsFollowed(function(l){
            userService.listfollowing = l;
            userService._amIFollowingListV(idList, returncallback)
          });
        } else {
          userService._amIFollowingV(idList, returncallback);
        }
    }

    userService._amIFollowingListV = function(idList, returncallback){
        var f = false;
        for (var i = userService.listfollowing.length - 1; i >= 0; i--) {
          var l = userService.listfollowing[i];
          if(l.idList == idList){
            f = true;
          }
        }
        returncallback(f);
    }

    userService.getListsFollowed = function(callback){
        $http.get('apiv1/list', {
              type: 'json'
            }).then(function(result){
                callback(result.data);
            },function(err){
                // do sometingh
            });
    }

    userService.followList = function(listp, callback){
        $http.get('apiv1/list/f/'+listp.idList, {
            type: 'json'
        }).then(function(result){
            userService.listfollowing = [];
            callback(result.data);
        },function(err){
            // do sometingh
        });
    }

    userService.unfollowList = function(listp, callback){
        $http.get('apiv1/list/uf/'+listp.idList, {
          type: 'json'
        }).then(function(result){
          userService.listfollowing = [];
          callback(result.data);
        },function(err){
          // do sometingh
        });
    }


    userService.findListById = function(idList, callback){
        $http.get('apiv1/list/'+idList, {
          type: 'json'
        }).then(function(result){
          userService.listfollowing = [];
          callback(result.data);
        },function(err){
          // do sometingh
        });
    }

    userDataService.isUserLogged();
    return userService;
}


function UserDataService(localStorageService){
    var userDataService = this;
    userDataService.getLoggedUser = function(){
        return userDataService.loggedUser;
    };

    userDataService.isUserLogged = function(){
        if (userDataService.loggedUser != null){
            return true;
        }
        if(localStorageService.get("lu") != null){
            userDataService.loggedUser = localStorageService.get("lu");
            return true;
        }

        return false;
    };

    userDataService.setUserLogged = function(user){
        userDataService.loggedUser = user;
        localStorageService.set("lu", user);
    };

    userDataService.getLoggedUser = function(){
        return userDataService.loggedUser;
    };
    userDataService.isMe = function(user){
        return userDataService.getLoggedUser().userEd.idUsuario == user.idUsuario;
    };
    userDataService.isUserLogged();
    return userDataService;
}

function UserMsgService($mdToast){
    var userMsgService = this;
    userMsgService.showToast = function(msg){
          $mdToast.show(
            $mdToast.simple()
              .textContent(msg)
              .hideDelay(3000)
          );
    }
    return userMsgService;
}

angular.module('rspApp')
	.factory('userDataService', ['localStorageService', UserDataService])
	.service('userService', ['$http', 'userDataService','$httpParamSerializer', UserService])
	.service('userMsgService', ['$mdToast', UserMsgService]);