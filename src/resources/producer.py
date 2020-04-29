import os
import json
import boto3
import datetime

def handler(event, context):

    events = boto3.client('events')
    event_bus = os.getenv("BUS_NAME", default = "edqm-bus")
    body = event["body"]
    keys = body.keys()
    if (
            'Source' not in keys or
            'DetailType' not in keys or
            'Detail' not in keys or
            'Resources' not in keys
    ):
        return {
            "status_code": 400,
            "status_msg": "Bad Request"
        }

    entry = {
        'Time': datetime.datetime.now().isoformat(),
        'Source': body['Source'],
        'Resources': body['Resources'],
        'DetailType': body['DetailType'],
        'Detail': body['Detail'],
        'EventBusName': event_bus
    }
    response = events.put_events(
        Entries=[
            entry
        ]
    )
    return response