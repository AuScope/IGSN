allControllers.controller('browseCtrl', ['$scope','$rootScope','$http','ViewSampleSummaryService',
                                  function ($scope,$rootScope,$http,ViewSampleSummaryService) {

	$scope.resetForm = function(){
   	 $scope.form={};   	 
   	 $scope.toggleFilter=false;
    }
    
    $scope.viewSample = function(name){
    	ViewSampleSummaryService.viewSample(name);
    }
	  
}]);