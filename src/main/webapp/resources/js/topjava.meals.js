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

$(document).ready(function(){
    $.datetimepicker.setLocale(locale);
    $("#dateTime").datetimepicker({format:"Y-m-d H:i"});
    $("#startTime").datetimepicker({datepicker:false,format:"H:i"});
    $("#endTime").datetimepicker({datepicker:false,format:"H:i"});
    $('#startDate').datetimepicker({
      format:'Y-m-d',
      onShow:function( ct ){
       this.setOptions({
        maxDate:jQuery('#endDate').val()?jQuery('#endDate').val():false
       })
      },
      timepicker:false
     });
     $('#endDate').datetimepicker({
      format:'Y-m-d',
      onShow:function( ct ){
       this.setOptions({
        minDate:jQuery('#startDate').val()?jQuery('#startDate').val():false
       })
      },
      timepicker:false
     });
});

function clearFilter() {
    $("#filter")[0].reset();
    $.get(mealAjaxUrl, updateTableByData);
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
                    "data": "description"
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
                $(row).attr("data-meal-excess", data.excess);
            }
        })
    );
});