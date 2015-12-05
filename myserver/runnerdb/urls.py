from django.conf.urls import url

from . import views

urlpatterns = [
    # ex: /runnerdb/
    url(r'^$', views.index, name='index'),
    # ex: /runnerdb/send_donation
    url(r'^send_donation/$', views.send_donation),
    # ex: /runnerdb/get_location
    url(r'^get_location/$', views.get_location),
    # ex: /runnerdb/send_location
    url(r'^send_location/$', views.send_location),
]