# ntua-streaming-iot


NTUA Thesis by Skourtsidis Giorgos. 
Supervisor: Paraskevas Kerasiotis

## Features
- Multiple producers/consumers using Spring's Beans
- Use spring profiles to change environment. (dev,test,prod)
- Export metrics to JMX, Prometheus, Graphite
- Start-up script
- Log compression and retention
- Status endpoints for app: `/app/info/status`, `/app/info/healch`, `/app/info/buildinfo` etc
- Write gc logs