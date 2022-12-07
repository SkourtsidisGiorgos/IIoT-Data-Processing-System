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


## Topic organization

```java
  temperature(0)   |   power(1)   |   pressure(2)
  -----------------|--------------|---------------
   0  |  1  |  2   |    0  |  1   |  0  |  1  |  2
  -----------------|--------------|---------------
  27.1| 155.4| 90.3|    638|  722 | 22.1| 12.8| 21.5
  27.3| 157.9| 89.6|    652|  713 | 21.9| 12.6| 21.8
  27.2| 156.2| 89.9|    640|  720 | 22.0| 12.7| 21.7
  27.1| 155.4| 90.3|    638|  722 | 22.1| 12.8| 21.5
  ```