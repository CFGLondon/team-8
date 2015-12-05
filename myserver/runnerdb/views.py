from django.shortcuts import render_to_response
#from polls.models import Poll
from django.http import JsonResponse

from Data import Runners
from twilio.rest import TwilioRestClient


def send_location(request):
    runner = json.loads(request.body)
    Runners[runner.id] = runner
def get_location():
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