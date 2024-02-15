const mealAjaxUrl = "ajax/meals/";

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


function filter(startDate, endDate, startTime, endTime) {
    $.ajax({
        url: ctx.ajaxUrl + "filter",
        type: "GET",
        data: {
            "startDate": startDate,
            "endDate": endDate,
            "startTime": startTime,
            "endTime": endTime
        }
    }).done(function (data) {
        updateDataTable(data);
    });
}

function clearFilter() {
    $("#filter").find(":input").val("");
    updateTable();
}