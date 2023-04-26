$(function () {
    $('.table-detailed').DataTable({
        paging: true,
        lengthChange: true,
        lengthMenu: [5, 7, 10, 25],
        stateSave: true,
        searching: true,
        ordering: true,
        info: true,
        autoWidth: false,
        responsive: true,
        language: {
            search: "Search",
            searchPlaceholder: "Free text search all columns",
        },
        keys: true,
        dom: '<"container-fluid"<"row"<"col"B><"col"f>>>rtip',
        buttons: [
            'pageLength', 'colvis', 'copy', 'csv', 'excel', 'print'
        ]
    });
});

$(function () {
    $('.table-simple').DataTable({
        paging: true,
        pageLength: 5,
        lengthChange: false,
        stateSave: true,
        searching: true,
        ordering: true,
        info: true,
        autoWidth: false,
        responsive: true,
    });
});