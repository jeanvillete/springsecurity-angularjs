angular.module( 'hello', [] )
  .controller( 'HomeController', function( $http ) {
    var vm = this;
    vm.greeting = { id : 'xxx', content : 'Hello World!' }

    $http.get( '/resource' ).then( function( response ) {
      vm.greeting = response.data;
    });
  });
