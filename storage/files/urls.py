from django.urls import path

from . import views

urlpatterns = [
    path(
        "upload/<int:user_id>/<int:folder_id>",
        views.FileUploadView.as_view(),
        name="upload_file",
    ),
]
