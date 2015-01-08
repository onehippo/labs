"use strict";
var productsApp = angular.module('productsApp', ['ngRoute', 'ngResource', 'ngSanitize']);

productsApp.constant('apiPrefix', 'http://www.demo.onehippo.com/restapi/');

productsApp.config(function ($routeProvider) {
    $routeProvider
            .when('/', {
                templateUrl: 'product-list.html',
                controller: 'ProductsController'
            })
            .when('/:path*', {
                templateUrl: 'detail.html',
                controller: 'ProductsController'
            })
            .otherwise('/');
});

productsApp.factory('ProductsService', function($resource, apiPrefix) {
    return {
        getList: function() {
            return $resource(apiPrefix + 'topproducts', {'_type': 'json'}).query();
        },
        getProductByPath: function (path) {
            return $resource(apiPrefix + path, {'_type': 'json'}).get();
        }
    }
});

productsApp.controller('ProductsController', function ($scope, $location, $routeParams, ProductsService, apiPrefix) {
    if(!$routeParams.path) {
        $scope.products = ProductsService.getList();
    } else {
        $scope.product = ProductsService.getProductByPath($routeParams.path);
    }

    $scope.showProduct = function (product) {
        if (product['links']) {
            var productByUrl = product['links'][0]['Link'].href;
            var shortUrl = productByUrl.substr(apiPrefix.length);
            return $location.path(shortUrl);
        }
    }
});
