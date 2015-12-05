from django.conf.urls import url

from . import views

urlpatterns = [
    # ex: /runnerdb/
    url(r'^$', views.index, name='index'),
    # ex: /runnerdb/5/send_donation
    url(r'^(?P<runner_id>[0-9]+)/send_donation/$', views.send_donation, name='send_donation'),
    # ex: /runnerdb/5/results/
    #url(r'^(?P<runner_id>[0-9]+)/results/$', views.results, name='results'),
    # ex: /runnerdb/5/vote/
    #url(r'^(?P<runner_id>[0-9]+)/vote/$', views.vote, name='vote'),
]