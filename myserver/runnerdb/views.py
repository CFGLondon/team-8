from django.shortcuts import render_to_response
#from polls.models import Poll
from django.http import JsonResponse, HttpResponse
from django.shortcuts import render
from django.template import RequestContext, loader

from Data import Runners
from twilio.rest import TwilioRestClient
from django.shortcuts import render_to_response

def index(request):
   return render_to_response('index.html')

def send_location(request):
    runner = json.loads(request.body)
    Runners[runner.id] = runner
    print(runner.x)
    print(runner.y)
    return HttpResponse("Location sent")

def get_location(request):
    return JsonResponse(Runners)

def send_donation(request):
    #This temporary idea demonstrates how to implement a vibration on a fitbit. The target phone is called, causing the fitbit
    #to vibrate
    account = "ACe1eb373d270917b737deaedb1ecda797"
    token = "c4909e963577d964791ef521a1195fb1"
    print("Setting up client")
    client = TwilioRestClient(account, token)

    print("Trying to make a call")
    call = client.calls.create(to="447933144812",
                                from_="441163260756",
                                url="https://demo.twilio.com/welcome/voice/")
    print(call.sid)
    return HttpResponse("HI HENRY!")