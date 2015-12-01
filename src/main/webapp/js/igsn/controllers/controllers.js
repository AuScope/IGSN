var app = angular.module('app', ['ngRoute','ngAnimate']);

app.controller('RegistrantCtrl', ['$scope','$rootScope','$http',
                                  function ($scope,$rootScope,$http) {

	$scope.authorizedUser=false;
	
	$scope.registrants=null;
	
	$scope.initialize=function() {
		$http.get('getAllRegistrant.do', {}).success(function(response) {
			$scope.registrants=response;
		  }).error(function(data) {
			  $scope.registrants=null;
		  });
		
		$http.get('getUser.do', {}).success(function(response) {
			 if(response.name){
				 $scope.authorizedUser=true
			 }else{
				 $scope.authorizedUser=false;
			 }		  
		  }).error(function(data) {
			  $scope.authorizedUser=false;
		  });
		
	
		 
    }

    $scope.initialize();
    
    $scope.submit = function(){
    	$http.get('addRegistrant.do',{
			params:{	
				name: $scope.submit.rname,
				email:$scope.submit.email,
				username:$scope.submit.username
				}
		 })     
	     .success(function(data) {
	    	 $scope.registrants=data;    
	     })
	     .error(function(data, status) {    	
	    	 alert("Error adding registrant");
	     })
    }
	  
}]);
