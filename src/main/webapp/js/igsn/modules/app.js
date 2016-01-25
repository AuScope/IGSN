var app = angular.module('app', ['ngRoute','allControllers','ngAnimate']);

app.config(['$routeProvider',
            function($routeProvider) {
              $routeProvider.
                when('/', {
              	  redirectTo: '/browse'
                
                }).
                when('/browse', {
                  templateUrl: 'views/browse.html'
               
                }).
                
                otherwise({
                  redirectTo: '/'
                });
              
}]);