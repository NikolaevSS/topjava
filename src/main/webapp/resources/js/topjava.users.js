// $(document).ready(function () {
$(function () {
    makeEditable({
            ajaxUrl: "ajax/admin/users/",
            datatableApi: $("#datatable").DataTable({
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
        }
    );

    $("input:checkbox.user-status").click(function () {
        enable($(this).closest("tr").prop("id"), this.checked)
    });
});

function enable(id, enabled) {
    $.ajax({
        url: context.ajaxUrl + "enable",
        type: "POST",
        data: "id=" + id + "&enabled=" + enabled
    }).done(function () {
        document.getElementById(id).setAttribute("data-user-enabled", enabled);
        successNoty("Change user status");
    });
}