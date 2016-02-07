var allControllers = angular.module('allControllers', []);

//SERVICE LEVEL CONTROLLERS
allControllers.controller('sampleDetailsCtrl', function ($scope, $modalInstance, params, modalService,$http,leafletData,$timeout) {
	
	$scope.form={};
	$scope.title = params.name;
	
	angular.extend($scope, {
	    center: {
	    	lat: -28,
	        lng: 135,
	        zoom: 4
	    },
	    defaults: {
            scrollWheelZoom: false
        },
	    markers: {
            osloMarker: {
            	lat: -25.2726333,
    	        lng: 114.9839476,
                message: "I want to travel here!",
                focus: true,
                draggable: false
            }
        }
	});
	
	leafletData.getMap("map").then(function (map) {
		
		$timeout(function(){
			map.invalidateSize()
		},500)
	
		 
	})
	
	
	
	
	
	
    $scope.ok = function () {	
		 $modalInstance.close();		 
	 };
	$scope.cancel = function () {
		    $modalInstance.dismiss('cancel');
	}; 
	  
});
