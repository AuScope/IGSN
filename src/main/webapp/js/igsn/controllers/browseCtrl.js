allControllers.controller('browseCtrl', ['$scope','$rootScope','$http','ViewSampleSummaryService','modalService','DropDownValueService',
                                  function ($scope,$rootScope,$http,ViewSampleSummaryService,modalService,DropDownValueService) {

	
	 
	$scope.form={};
	
	
    
    $scope.viewSample = function(name){
    	ViewSampleSummaryService.viewSample(name);
    }
    
    DropDownValueService.getSampleType()
	.then(function(data) {
		 $scope.sampleTypes= data;
	}, function(data, status) {
		 modalService.showModal({}, {    	            	           
	           headerText: "Error retrieve user list",
	           bodyText: data
		 });
	});
    
    $scope.totalItem = 0;
	$scope.currentPages = 1;
    
    $scope.searchSample = function(page){
		$scope.currentPages = page;//VT page is reset to 1 on new search
		var params ={	
				igsn: $scope.form.igsn,
				sampleType:$scope.form.sampleType,
				pageNumber:page,
				pageSize:10
				}
		
		//VT: Actual results
		$http.get('search.do',{
			params:params
		 })     
	     .success(function(data) {
	       $scope.samples = data;       
	       $scope.toggleFilter=false;
	     })
	     .error(function(data, status) {    	
	    	 modalService.showModal({}, {    	            	           
		           headerText: "Error loading data:" + status ,
		           bodyText: "Please contact cg-admin@csiro.au if this persist"
	    	 });
	       
	     })
	     //VT: Get the count of the result
	     $http.get('searchCount.do',{
			params:params
		 })     
	     .success(function(data) {
	       $scope.totalItem = data;       	        
	     })
	     .error(function(data, status) {    	
	    	 modalService.showModal({}, {    	            	           
		           headerText: "Error loading data:" + status ,
		           bodyText: "Please contact cg-admin@csiro.au if this persist"
	    	 });
	       
	     })
	}
    
    $scope.resetForm = function(){    	 
	   	 $scope.toggleFilter=false;
	   	 $scope.samples=[];
	   	 $scope.form={};
	   	 $scope.searchSample(1);
	 }
	
	$scope.pageChanged = function() {
		$scope.searchSample($scope.currentPages);
	  };
	  
	  $scope.pageChanged();
	  
}]);