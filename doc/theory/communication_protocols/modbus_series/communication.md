Each Modbus message has the same structure.
Four basic elements are present in each message.
The sequence and order of these elements are the same for all messages.
This allows for easy parsing of the content of the Modbus message.
A conversation is always started by a master in the Modbus network.
A Modbus master sends a message,
and depending on the contents of the message,
the slave interprets the message and responds to it.
Physical slave addressing in the message header
is used to define which slave device should respond to a message.
All other nodes on the Modbus network
ignore the message if the address field doesn't match their own address.
Modbus functions perform read and write
instructions to the slave’s internal memory registers
to configure, monitor, and control the slave’s inputs and outputs.
Modbus devices will typically include a register map
outlining where the configuration,
input and output data can be written and read from.
You should always refer to the slave’s register map of your device
to gain a better understanding of its overall operation.
Discrete Inputs, Coils Outputs, Input Registers (Input Data),
and Holding Registers (Output Data).
The service request area of the message
or Modbus Protocol Data Unit or PDU is comprised of a function code,
and a number of data bytes requested by the master.
The Modbus memory registers of a device
are organized around the four basic data reference types
and this data type is further identified
by the leading number used in the devices memory address,
such as, a 0 based register
referencing a message to Read or Write discrete outputs or coils,
or a 1 based register referencing Reading discrete inputs,
or a 3 based register referencing Reading input registers,
and a 4 based register referencing Reading or Writing to output or holding registers.
The function code field specifies which register data group
it reads or writes to and from the slave.
For example, a Function code 03, read Holding Registers 40001-40002
is addressed as data register 0000
in the data address field of the message sent to the slave.
The function code 03 works on this Holding Register type in the slave’s data map
because the request specifies using a holding register data type operation
and this type of addressing in the request is implied.
The fields in the PDU are broken down into bytes and grouped by the field name.
The request message contains Function code of 03,
the starting address HI and LO bytes of address 0000
and the count number of addresses to read from the slave,
register HI and LO bytes of count value 0002 specifies the starting register
and quantity of registers to be read from the slave.
Example of a request to read the first two registers in the Holding Register area
0...1 (register 40001 to 40002)
and therefore, as another example,
the holding register 40108 is actually addressed
as register 108 in the message data area of the PDU.
Many of the data types are named from its use in driving relays,
for example, a single-bit physical output is called a coil,
and a single-bit physical input is called a discrete input or a contact.
The function code field of the message
will contain one byte that tells the slave what kind of action to take.
Valid function codes are range from 1 to 255,
but not all codes will apply to a particular slave.
This table highlights a subset of standard Modbus functions.
In addition, the master request data field,
provides the slave with any additional information required by the slave
to complete the action specified by the function code in the master’s request,
typically including the slave map register address,
the number of registers to provide in the request,
and any write data from the master.
The slave’s normal response simply echoes the original function code of the request,
but the slave’s error response
returns a code that is equivalent to the original function code
with the most significant bit set to logic 1.
For example, the Read Holding Registers command
has the function code with 1 byte containing 8 bits is binary 0000 0011.
If the slave device accepts the request without error
it will return the same code in its response.
However, if an error occurs, the slave will return 1 byte containing 8 binary bits
1000 0011 in the function code field
and appends a unique code in the data field of the response message
that tells the master device what kind of error occurred,
or the reason for the error.
This function code 01, Read Coils code,
is used to read from 1 to 2000 contiguous registers
for the status of coils in a slave device.
The Request PDU specifies the starting address of the slave’s memory address of the first coil,
and the number of coils to read from the slave device.
The function code 02, Read Discrete Inputs code,
is used to read from 1 to 2000 contiguous status of discrete inputs in a remote slave.
The Request PDU specifies the starting address
of the slave’s memory address of the first input,
and the number of coils to read from the slave device.
The function code 03, Read Holding Registers code,
is used to read the contents of a contiguous block of holding registers in a remote slave.
The Request PDU specifies the starting register address
and the number of registers to read from the slave device.
The function code 04, Read Input Registers code,
is used to read from 1 to 125 contiguous input registers in a remote device.
The Request PDU specifies the starting register address and the number of registers.
The function code 05, Write Single Coil code,
is used to write a single output to either ON or OFF in a remote slave device.
The function code 06, Write Single Register code,
is used to write a single holding register in a remote slave device.
The Request PDU specifies the address of the slave memory register address to be written to.
The function code 15, Write Multiple Coils code,
is used to force each coil in a sequence of coils
to either ON or OFF in a remote slave device.
The Request PDU specifies the coils memory address to be forced ON or OFF.
And the function code 16,
Write Multiple registers is used to write a block of contiguous registers
(1 to 123 registers) in a remote slave device.
While these Modbus function codes represent the most common read and write functions,
it would be helpful for you to review the Modbus specification for additional information.
For additional detailed Modbus protocol technical information
please refer to the modbus.org website.
Communication with slave devices or master PLCs or computers
can be accomplished with Modbus simulator software
to run on your personal computer.
The connection can be either serial or ethernet
and in the form of a master or slave.
The software will allow you to perform all of the Modbus Protocol Communication function code
to simply read or write to an existing slave.
You can set up one PC to run the slave simulation software
and another to run the Master simulation software.
Connecting to a slave is first performed
by setting the communication parameters for your serial COM port.
And then by entering the slave’s address in the Device ID field,
for example 1, selecting the function code 01
to read the slave’s Coil Status
stating at address 0001 and reading a length of 100 coils.
In addition, there are several companies providing products and support
to help you with protocol communication.
ProSoft Technologies, HMS Industrial Communication,
and MOXA are to name just a few.