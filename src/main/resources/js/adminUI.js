(function(AJS, $) {

    var baseUrl = AJS.contextPath();

    AJS.toInit(function() {
        $.ajax({
            url: baseUrl + "/rest/admin-ui/1.0/",
            type: "GET",
            dataType: "json"
        }).done(function(config) { // when the configuration is returned...
            window.alert("JS async GET: " + config.name + ", " + config.age);
        });
    });

})(window.AJS, window.AJS.$ || window.jQuery);