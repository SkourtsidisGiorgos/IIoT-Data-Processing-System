from pymodbus.client import ModbusTcpClient

client = ModbusTcpClient('127.0.0.1', port=5020)
print(client)
client.connect()
client.write_coil(1, True)
# check if client is connect
print(client.is_socket_open())
result = client.read_coils(1,1)
if (result.isError()):
    print("Error: " + str(result))
else:
    print(result.bits[0])


# write register

for i in range(1, 4):
    result = client.write_register(i, i*11)
    print(result)
client.close()
