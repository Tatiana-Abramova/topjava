const mealAjaxUrl = "ajax/meals/";
let filterForm = $('#filterForm');

// https://stackoverflow.com/a/5064235/548473
const ctx = {
    ajaxUrl: mealAjaxUrl
};

// $(document).ready(function () {
$(function () {
    makeEditable(
        $("#datatable").DataTable({
            "paging": false,
            "info": true,
            "columns": [
                {
                    "data": "dateTime"
                },
                {
                    "data": "description"
                },
                {
                    "data": "calories"
                },
                {
                    "defaultContent": "Edit",
                    "orderable": false
                },
                {
                    "defaultContent": "Delete",
                    "orderable": false
                }
            ],
            "order": [
                [
                    0,
                    "asc"
                ]
            ]
        })
    );
});


function filter() {
    $.ajax({
        url: ctx.ajaxUrl + "filter",
        type: "GET",
        data: {
            "startDate": filterForm.find('input[name=\'startDate\']').val(),
            "endDate": filterForm.find('input[name=\'endDate\']').val(),
            "startTime": filterForm.find('input[name=\'startTime\']').val(),
            "endTime": filterForm.find('input[name=\'endTime\']').val()
        }
    }).done(function (data) {
        updateDataTable(data);
    });
}

function clearFilter() {
    filterForm[0].reset();
    updateTable();
}