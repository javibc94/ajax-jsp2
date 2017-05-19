/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//Angular code
(function () {
    //Application module

    angular.module('pharmacyApp').controller("PurchaseController", ['$http', '$scope', '$window', '$cookies', 'accessService', 'userConnected', function ($http, $scope, $window, $cookies, accessService, userConnected) {
        $scope.purchase = new Purchase();
        $scope.purchasesArray = new Array();
        //pagination
        $scope.currentPage=1;
        $scope.pageSize = 5;
        
        //Scope variables
        $scope.specialRequests = ["Dlivery at the main hospital", "Fragil material, must be sended in a special vehicle", "Product easily contamined, special protection nedded"];
        
        //Date pickers scope variables and functions
        $scope.minDeliveryDate = new Date((new Date()).setDate((new Date()).getDate() + 1));
        $scope.formats = ['dd-MMMM-yyyy', 'yyyy/MM/dd', 'dd.MM.yyyy', 'shortDate'];
        $scope.format = $scope.formats[0];
        $scope.dateOptions = {
            dateDisabled: "",
            formatYear: 'yyyy',
            maxDate: "",
            minDate: $scope.minDeliveryDate,
            startingDay: 1
        };

        $scope.deliveryDate = {
            opened: false
        };

        $scope.openDeliveryDate = function () {
            $scope.deliveryDate.opened = true;
        };
        
        $scope.specialReqMng = function (indexChecked) {
            if($("#specialReq"+indexChecked).is(":checked")) {
                $scope.purchase.addSpecialRequests($scope.specialRequests[indexChecked]);
            } else {
                $scope.purchase.removeSpecialRequests($scope.specialRequests[indexChecked]);
            }
        }
        
        this.addPurchase = function () {
            $scope.purchase.setIdUser($scope.$parent.idUser); //Get user id from session controller
            $scope.purchase.setIdProduct($scope.selectedProduct.id);
            $scope.purchase = angular.copy($scope.purchase);

            // Server conenction to verify user's data.
            var promise = accessService.getData("MainController",
                    true, "POST", {controllerType: 2, action: 10000, JSONData: JSON.stringify($scope.purchase)});

            promise.then(function (outputData) {
                
                if (outputData[0] === true) {
                    var purchaseObj = new Purchase();
                    purchaseObj.construct(outputData[1].id, $scope.$parent.idUser, $scope.selectedProduct.id, $scope.purchase.deliveryDate, $scope.purchase.specialRequests, $scope.purchase.specialInstructions);
                } else {
                    if (angular.isArray(outputData[1])) {
                        console.log(outputData);
                    }
                }
            });
        }
        
        this.updatePurchase = function (){
            $scope.purchasesArray = angular.copy($scope.purchasesArray);
            //console.log($scope.reviewsArray);
            var promise = accessService.getData("MainController",
                    true, "POST", { controllerType: 2, action: 10100, JSONData: JSON.stringify($scope.purchasesArray) });

            promise.then(function (outPutData) {
                if (outPutData[0] === true) {
                            alert("Updated sucessfully");
                } else {
                    if (angular.isArray(outPutData[1])) {
                        alert(outPutData[1]);
                    } else { alert("There has been an error in the server, try later"); }
                }

            });
        }
        this.deletePurcahse = function (index){
            var removePurchase = new Product();
            removePurchase = angular.copy($scope.purchasesArray[index]);
            $scope.purchasesArray.splice(index, 1);

            var promise = accessService.getData("MainController",
                true, "POST", { controllerType: 2, action: 10200, JSONData: JSON.stringify(removePurchase) });

            promise.then(function (outputData) {
                //console.log(outputData);
                if (outputData[0] === true) {
                    alert("Removed succesfully");
                } else {
                    if (angular.isArray(outputData[1])) {
                        alert(outPutData[1]);
                    } else {
                        alert("There has been an error in the server, try later");
                    }
                }
            });

        }
        
        
    }]);

})();

