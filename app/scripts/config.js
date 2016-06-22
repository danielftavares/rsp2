angular.module('rspApp')
	.config(["$routeProvider", function($routeProvider){
	$routeProvider.
	  when('/login', {
	    templateUrl: 'views/login.html'
	  }).
	  when('/main', {
	    templateUrl: 'views/main.html',
	    controller: 'MainCtrl'
	  }).
	  when('/user/:iduser/:username', {
        templateUrl: 'views/user.html',
        controller: 'UserCtrl'
      }).
      when('/editprofile', {
          templateUrl: 'views/useredit.html',
          controller: 'UserEditCtrl'
        }).
	  otherwise({
	    redirectTo: '/login'
	  });
}]);

angular.module('rspApp')
    .config(['localStorageServiceProvider', function (localStorageServiceProvider) {
      localStorageServiceProvider.setPrefix('rsp');
    }]);