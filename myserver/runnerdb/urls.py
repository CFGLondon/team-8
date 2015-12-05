from django.conf.urls import url
from django.views.generic import TemplateView

from . import views

urlpatterns = [
    # format: /runnerdb/
    url(r'^$', TemplateView.as_view(template_name='index.html')),
    # format: /runnerdb/send_donation
    url(r'^send_donation/$', views.send_donation),
    # format: /runnerdb/get_location
    url(r'^get_location/$', views.get_location),
    # format: /runnerdb/send_location/?runner_id=0&x=0&y=0
    url(r'^send_location/$', views.send_location),
]