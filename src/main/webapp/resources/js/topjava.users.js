const userAjaxUrl = "admin/users/";

// https://stackoverflow.com/a/5064235/548473
const ctx = {
    ajaxUrl: userAjaxUrl
};

// $(document).ready(function () {
$(function () {
    makeEditable(
        $("#datatable").DataTable({
            "paging": false,
            "info": true,
            "columns": [
                {
                    "data": "name"
                },
                {
                    "data": "email"
                },
                {
                    "data": "roles"
                },
                {
                    "data": "enabled"
                },
                {
                    "data": "registered"
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

function toggle(enable, id) {
    $.ajax({
        type: "PUT",
        url: ctx.ajaxUrl + id + "?enabled=" + enable.checked,
        contentType: "application/json; charset=UTF-8",
        data: {enabled: enable.checked}, // не получается передать параметр через data, пробовала разные форматы. В браузере видно, что параметр передается, но контроллер его не видит
    }).done(function () {
        updateTable();
        status = enable.checked ? "enabled" : "disabled"
            successNoty("User " + status);
    });
}