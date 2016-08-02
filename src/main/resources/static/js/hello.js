angular.module( 'hello', [ 'ngRoute' ] )
	.config( function( $routeProvider, $httpProvider ){
		$routeProvider.when( '/', {
			templateUrl : 'home.html',
			controller : 'HomeController',
			controllerAs : 'vm'
		}).when( '/login', {
			templateUrl : 'login.html',
			controller : 'NavigationController',
			controllerAs : 'vm'
		}).otherwise( '/' );
		
		$httpProvider.defaults.headers.common[ "X-Requested-With" ] = 'XMLHttpRequest';
	}).controller( 'HomeController', function( $http ) {
		var vm = this;
		vm.greeting = { id : 'xxx', content : 'Hello World!' }
		
		$http.get( '/resource' ).then( function( response ) {
			vm.greeting = response.data;
		});
	}).controller( 'NavigationController', function( $rootScope, $http, $location ){
		var vm = this;
		vm.logout = logout;
		vm.credentials = {};
		vm.login = login;
		
		authenticate();
		
		//////////
		function logout() {
			$http.post( 'logout', {} ).finally( function(){
				$rootScope.authenticated = false;
				$location.path( "/" );
			});
		};
		
		function authenticate( credentials, callback ) {
			var headers = credentials ? { authorization : "Basic " + btoa( credentials.username + ":" + credentials.password ) } : {};
			$http.get( 'user', { headers : headers } ).then( function( response ){
				$rootScope.authenticated = response.data.name && true;
				callback && callback();
			}, function(){
				$rootScope.authenticated = false;
				callback && callback();
			});
		};
		
		function login() {
			authenticate( vm.credentials, function(){
				if ( $rootScope.authenticated ) {
					$location.path( "/" );
					vm.error = false;
				} else {
					$location.path( "/login" );
					vm.error = true;
				}
			});
		};
	});