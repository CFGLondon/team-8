from django.shortcuts import render_to_response
#from polls.models import Poll
from django.http import JsonResponse

from Data import Runners

def send_location(request):
    runner = json.loads(request.body)
    Runners[runner.id] = runner
def get_location():
    return JsonResponse(Runners) 
def send_donation(request):
    pass