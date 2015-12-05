from django.shortcuts import render, get_object_or_404
from django.http import HttpResponse
from models import Runner


def index(request):
    return HttpResponse("Hello, world. You're at the runner_id index.")
