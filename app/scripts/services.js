function UserService($http, localStorageService){
    var userService = this;

    userService.isUserLogged = function(){
        if (userService.loggedUser != null){
            return true;
        }
        if(localStorageService.get("lu") != null){
            userService.loggedUser = localStorageService.get("lu");
            return true;
        }

        return false;
    };

    userService.getLoggedUser = function(){
        return userService.loggedUser;
    };
    userService.isMe = function(user){
        return userService.getLoggedUser().userEd.idUsuario == user.idUsuario;
    };

    userService.login = function(username, password, callback){
        $http(
            {
                method  : 'POST',
                url: "apiv1/user/login",
                data    : {"username": username, "password": password }
            }
        ).then(function successLogin(response) {
            userService.loggedUser = response.data;
            localStorageService.set("lu", response.data);
            callback(response.data);
        }, function errorLogin(response) {
            // TODO
        });
    };

    userService.listPosts = function(info, callback){
        $http(
            {
                method  : 'GET',
                url: '/rsp/apiv1/post',
                params: info,
                headers: {
                  'Authorization': 'RSPUT '+ userService.loggedUser.userEd.idUsuario + ':' + userService.loggedUser.token
                },
                responseType: "json"
            }
        ).then(function success(response) {
                     callback(response.data);
         }, function error(response) {
             // TODO
         });
    };

    userService.post = function(formData, callback){
        $http.post('/rsp/apiv1/post', formData, {
            headers: {
              'Authorization': 'RSPUT '+ userService.loggedUser.userEd.idUsuario + ':' + userService.loggedUser.token,
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
                    url: '/rsp/apiv1/user/'+idUser,
                    headers: {
                      'Authorization': 'RSPUT '+ userService.loggedUser.userEd.idUsuario + ':' + userService.loggedUser.token
                    },
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
                url: '/rsp/apiv1/profile/'+iduser,
                headers: {
                  'Authorization': 'RSPUT '+ userService.loggedUser.userEd.idUsuario + ':' + userService.loggedUser.token
                },
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
                url: '/rsp/apiv1/user/f/'+user.idUsuario,
                method: 'GET',
                headers: {
                    'Authorization': 'RSPUT '+ userService.loggedUser.userEd.idUsuario + ':' + userService.loggedUser.token
                }
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
                url: '/rsp/apiv1/user/uf/'+user.idUsuario,
                method: 'GET',
                headers: {
                    'Authorization': 'RSPUT '+ userService.loggedUser.userEd.idUsuario + ':' + userService.loggedUser.token
                }
            } ).then(function success(response) {
                userService.following = [];
                callback(response.data);
            }, function error(response) {
                // TODO
            });
    }

    userService.following = [];

    userService.listFollowingAndFollowers = function(idUser, callback){
        $http(
            {
                method  : 'GET',
                url: '/rsp/apiv1/user/lf/'+idUser,
                responseType: 'json',
                headers: {
                  'Authorization': 'RSPUT '+ userService.loggedUser.userEd.idUsuario + ':' + userService.loggedUser.token
                }
            }
           ).then(function success(response) {
                       callback(response.data);
           }, function error(response) {
               // TODO
           });
      }

    userService.amIFollowing = function(idUser, callbackf){
        if(userService.following.length == 0){
          userService.listFollowingAndFollowers(userService.loggedUser.userEd.idUsuario,
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
                        url: '/rsp/apiv1/profile/f',
                        responseType: 'json',
                        headers: {
                          'Authorization': 'RSPUT '+ userService.loggedUser.userEd.idUsuario + ':' + userService.loggedUser.token
                        }
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

            $http.post('/rsp/apiv1/user/profile', profileForm, {
                headers: {
                  'Authorization': 'RSPUT '+ userService.loggedUser.userEd.idUsuario + ':' + userService.loggedUser.token,
                  'Content-Type': undefined
                },
                transformRequest: angular.identity
            }).then(function(result){
                callback();
            },function(err){
                // do sometingh
            });


    }

    userService.isUserLogged();
    return userService;
}

angular.module('rspApp')
	.service('userService', ['$http', 'localStorageService', UserService])