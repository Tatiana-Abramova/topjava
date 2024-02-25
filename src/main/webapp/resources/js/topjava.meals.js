const mealAjaxUrl = "profile/meals/";
const locale = i18n["app.locale"];

// https://stackoverflow.com/a/5064235/548473
const ctx = {
    ajaxUrl: mealAjaxUrl,
    updateTable: function () {
        $.ajax({
            type: "GET",
            url: mealAjaxUrl + "filter",
            data: $("#filter").serialize()
        }).done(updateTableByData);
    }
};

function clearFilter() {
    $("#filter")[0].reset();
    $.get(mealAjaxUrl, updateTableByData);
}

function pickDateTime(element) {
    $.datetimepicker.setLocale(locale);
    $("#" + element.id).datetimepicker({format:"Y-m-d H:i"});
}

function pickDate(element) {
    $.datetimepicker.setLocale(locale);
    $("#" + element.id).datetimepicker({timepicker:false,format:"Y-m-d"});
}

function pickTime(element) {
    $.datetimepicker.setLocale(locale);
    $("#" + element.id).datetimepicker({datepicker:false,format:"H:i"});
}

$(function () {
    makeEditable(
        $("#datatable").DataTable({
            "ajax": {
                "url": mealAjaxUrl,
                "dataSrc": ""
            },
            "paging": false,
            "info": true,
            "columns": [
                {
                    "data": "dateTime",
                    "render": function (dateStr, type, row) {
                        if (type === "display") {
                            return formatDateTime(dateStr);
                        }
                        return dateStr;
                    }
                },
                {
                    "data": "description",

                },
                {
                    "data": "calories"
                },
                {
                    "orderable": false,
                    "defaultContent": "",
                    "render": renderEditBtn
                },
                {
                    "orderable": false,
                    "defaultContent": "",
                    "render": renderDeleteBtn
                }
            ],
            "order": [
                [
                    0,
                    "desc"
                ]
            ],
            "createdRow": function (row, data, dataIndex) {
                if (data.excess) {
                    $(row).attr("data-meal-excess", true);
                } else {
                    $(row).attr("data-meal-excess", false);
                }
            }
        })
    );
});