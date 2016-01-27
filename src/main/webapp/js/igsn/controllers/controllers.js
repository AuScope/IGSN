var allControllers = angular.module('allControllers', []);

//SERVICE LEVEL CONTROLLERS
allControllers.controller('sampleDetailsCtrl', function ($scope, $modalInstance, params, modalService,$http) {
	
	$scope.form={};
	$scope.title = params.name;

	
    $scope.ok = function () {	
		 $modalInstance.close();		 
	 };
	  
});
