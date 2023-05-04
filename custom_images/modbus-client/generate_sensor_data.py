from pymodbus.client import ModbusTcpClient
from numpy.random import randn
from random import randint
import time
import numpy as np
import pandas as pd
from datetime import datetime, timedelta

np.random.seed(42)
start_date = datetime.now()
end_date = start_date + timedelta(days=1)
# Generate the time series index
date_range = pd.date_range(start_date, end_date, freq='S')

client = ModbusTcpClient('modbus-server', port=5020)
print(client)
client.connect()

# check if client is connect
print(client.is_socket_open())
result = client.read_coils(1,1)
if (result.isError()):
    print("Error: " + str(result))
else:
    print(result.bits[0])


# Default:
# temperature  = 4 sensors
# power = 3 sensors
# pressure = 3 sensors
# 4 + 3 + 3  = 10
temp_sensors = 4
power_sensors = 3
pressure_sensors = 3



# coil will be active/inactive
# Modbus usually got 65,536 addresses/registers
# for each sensor we write 1 registers: it's current value. Each sensor has an id, same as the address.
# for each sensor we keep information about  it's category, geographical location, etc etc. We search info using it's id

temp_val = dict()
power_val = dict()
pressure_val = dict()
# Define the mean pressure and standard deviation for 3 pressure sensors
mean_pressure = [80,50, 100]
std_dev_pressure = [15, 5, 40]

# Define the mean power and standard deviation for 3 power sensors
mean_power = [1000, 3000, 1700]
std_dev_power = [300, 1600, 500]

# Define the mean temperature and standard deviation for 4 temperature sensors
mean_temperature = [50, 26, 33, 28]
std_dev_temperature = [13, 13, 7, 4]

# Generate random pressure, power, and temperature data for each sensor with daily seasonality
n = len(date_range)
pressure_data = []
power_data = []
temperature_data = []
for i in range(3):
    big_addup = randint(1, 10)
    daily_seasonality_pressure = 2 * np.sin(2 * np.pi * np.arange(n) / 86400)
    pressure_sensor_data = mean_pressure[i] + std_dev_pressure[i] * np.random.randn(n) + daily_seasonality_pressure + randint(-10, 20) * np.heaviside(randint(-1,1), randint(-1,1))
    if big_addup == 5:
        pressure_sensor_data += 12 * i
    pressure_data.append(pressure_sensor_data)

    daily_seasonality_power = 30 * np.sin(2 * np.pi * np.arange(n) / 86400)
    power_sensor_data = mean_power[i] + std_dev_power[i] * np.random.randn(n) + daily_seasonality_power + randint(-500, 500) * np.heaviside(randint(-1,1), randint(-1,1))
    if big_addup == 8:
        power_sensor_data += 200 * i
    power_data.append(power_sensor_data)

for i in range(4):
    big_addup = randint(1, 30)
    daily_seasonality_temperature = 3 * np.sin(2 * np.pi * np.arange(n) / 86400)
    temperature_sensor_data = mean_temperature[i] + std_dev_temperature[i] * np.random.randn(n) + daily_seasonality_temperature + randint(-5, 10) * np.heaviside(randint(-1,1), randint(-1,1))
    if big_addup == 15:
         temperature_data += 20
    temperature_data.append(temperature_sensor_data)
         
         

# Create pandas DataFrames with the pressure, power, and temperature data
pressure_df = pd.DataFrame(data=np.column_stack(pressure_data), index=date_range, columns=[f'Pressure_Sensor_{i}' for i in range(1, 4)])
power_df = pd.DataFrame(data=np.column_stack(power_data), index=date_range, columns=[f'Power_Sensor_{i}' for i in range(1, 4)])
temperature_df = pd.DataFrame(data=np.column_stack(temperature_data), index=date_range, columns=[f'Temperature_Sensor_{i}' for i in range(1, 5)])

for idx, (pressure_row, power_row, temperature_row) in enumerate(zip(pressure_df.iterrows(), power_df.iterrows(), temperature_df.iterrows())):
    print(f"{date_range[idx]}:")
    _, pressure_values = pressure_row
    _, power_values = power_row
    _, temperature_values = temperature_row
    for i in range(1, 4):
            # Write the pressure value to the corresponding Modbus register
            pressure_value = pressure_values[f'Pressure_Sensor_{i}']
            print(f"  Pressure Sensor {i}: {pressure_value} hPa")
            try:
                result = client.write_register(i, int(pressure_value))
            except Exception as e:
                print(e.__traceback__)

            # Write the power value to the corresponding Modbus register
            power_value = power_values[f'Power_Sensor_{i}']
            print(f"  Power Sensor {i}: {power_value} W")
            try:
                result = client.write_register(power_sensors + i, int(power_value))
            except Exception as e:
                print(e.__traceback__)

    for i in range(1, 5):
            # Write the temperature value to the corresponding Modbus register
            temperature_value = temperature_values[f'Temperature_Sensor_{i}']
            print(f"  Temperature Sensor {i}: {temperature_value} °C")
            try:
                result = client.write_register(temp_sensors + power_sensors + i, int(temperature_value))
            except Exception as e:
                print(e.__traceback__)
    time.sleep(1)

client.close()
