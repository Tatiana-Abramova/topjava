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
        type: "POST",
        url: ctx.ajaxUrl + id,
        contentType: "application/x-www-form-urlencoded",
        data: {enabled: enable.checked},
    }).done(function (data) {
        document.getElementById(id).className = enable.checked ? "" : "text-black-50";
        status = enable.checked ? "enabled" : "disabled"
        successNoty("User " + status);
    }).fail(function (jqXHR, textStatus, errorThrown) {
        enable.checked = !enable.checked;
    })
}