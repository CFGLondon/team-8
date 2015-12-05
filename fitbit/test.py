from twilio.rest import TwilioRestClient

account = "ACe1eb373d270917b737deaedb1ecda797"
token = "c4909e963577d964791ef521a1195fb1"
print("Setting up client")
client = TwilioRestClient(account, token)

print("Trying to make a call")
call = client.calls.create(to="447933144812",
                            from_="441163260756",
                            url="https://demo.twilio.com/welcome/voice/")
print(call.sid)
