from django.db import models

# runner model that we later didn't use
class Runner(models.Model):
    runner_id = models.IntegerField()
    donation_received = models.BooleanField()
    location_x = models.FloatField()
    location_y = models.FloatField()

    def __str__(self):
        return "runner_id: " + str(self.runner_id)
