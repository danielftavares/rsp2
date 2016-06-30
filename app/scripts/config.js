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
      when('/criarlista', {
          templateUrl: 'views/listnew.html',
          controller: 'ListNewCtrl'
        }).
      when('/lista/:idList/:listname', {
          templateUrl: 'views/list.html',
          controller: 'ListCtrl'
        }).
	  otherwise({
	    redirectTo: '/login'
	  });
}]);


angular.module('rspApp')
.config(["$mdThemingProvider", function($mdThemingProvider) {


//  $mdThemingProvider.definePalette('rspPalette', {
//    '50': '#e1f5fe',
//    '100': '#b3e5fc',
//    '200': '#81d4fa',
//    '300': '#4fc3f7',
//    '400': '#29b6f6',
//    '500': '#000000', // toolbar!
//    '600': '#039be5',
//    '700': '#0288d1',
//    '800': '#0277bd',
//    '900': '#01579b',
//    'A100': '#80d8ff',
//    'A200': '#40c4ff',
//    'A400': '#00b0ff',
//    'A700': '#0091ea',
//    'contrastDefaultColor': 'dark',
//    'contrastLightColors': '600 700 800 900 A700',
//     'contrastStrongLightColors': '600 700 800 A700'
//  });

    $mdThemingProvider.theme('default')
    .primaryPalette('grey',  {
                                   'default': '900' // use shade 200 for default, and keep all other shades the same
                                 });
}]);

angular.module('rspApp')
    .config(['localStorageServiceProvider', function (localStorageServiceProvider) {
      localStorageServiceProvider.setPrefix('rsp');
    }]);

angular.module('rspApp')
    .config(['$httpProvider', function($httpProvider) {

        var interceptor = [
          '$q',
          '$rootScope',
          'userDataService',
          function($q, $rootScope, userDataService) {

            var service = {
              // run this function before making requests
              'request': function(config) {
                  if(userDataService.isUserLogged()){
                    config.headers['Authorization'] = 'RSPUT '+ userDataService.getLoggedUser().userEd.idUsuario + ':' + userDataService.getLoggedUser().token;
                  }
                  return config;
              },
              'responseError': function(response){
                $rootScope.$broadcast( 'rspHttpError', response);
              }
            };

            return service;
          }
        ];

        $httpProvider.interceptors.push(interceptor);
    }]);