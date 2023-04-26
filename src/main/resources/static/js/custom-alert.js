
// Custom function to show Toastr notifications
// type should be either "success", "error", "warning"
function showToastrNotification(type, message) {
    toastr.options = {
        closeButton: true,
        progressBar: true,
        positionClass: 'toast-top-right',
        showDuration: '300',
        hideDuration: '1000',
        timeOut: '5000',
        extendedTimeOut: '1000',
        showEasing: 'swing',
        hideEasing: 'linear',
        showMethod: 'fadeIn',
        hideMethod: 'fadeOut'
    };

    if (type === 'success') {
        toastr.success(message);
    } else if (type === 'error') {
        toastr.error(message);
    } else if (type === 'warning') {
        toastr.warning(message);
    } else {
        toastr.info(message);
    }
}

// you can use the showToastrNotification function in your custom JavaScript file to display Toastr notifications.
// Just make sure the order of your script tags is correct, and jQuery, Toastr, and your custom script are all included in your HTML file.
