const filterId = "filter";
const filterForm = $("#".concat(filterId));

function clearFilter() {
    document.getElementById(filterId).reset();
    updateTable();
}

function filter() {
    $.ajax({
        type: "GET",
        url: context.ajaxUrl.concat(filterId),
        data: filterForm.serialize()
    }).done(function (data) {
        context.datatableApi.clear().rows.add(data).draw();
    });
}

$(function () {
    makeEditable({
            ajaxUrl: "ajax/meals/",
            datatableApi: $("#datatable").DataTable({
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
        }
    );
});