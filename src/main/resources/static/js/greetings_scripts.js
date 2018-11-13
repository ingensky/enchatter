document.getElementById("clearButton").onclick = function (e) {
    document.getElementById("greeting").value = "";
}

$('#greetingsForm').on('show.bs.modal', function(event) {
    var button = $(event.relatedTarget)
    $scope.$apply(function() {
        $scope.userId = button.data('whatever')
    });
});