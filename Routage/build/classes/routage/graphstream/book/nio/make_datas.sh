#!/bin/sh
#
# This script will take the ByteArray class that can handle only byte arrays
# and make several classes able to contain a given base type (int, short, long,
# float, double).
#

types="Short:2:short Int:4:int Long:8:long Float:4:float Double:8:double"

#
# First create all the ByteBuffer counterparts
#

for t in ${types}
do
	#
	# Extract the Java type and its size in bytes.
	#
	type=${t#*:}
	size=${type%%:*}
	type=${type##*:}

	#
	# Extract the corresponding nio Buffer name.
	#
	Type=${t%%:*}

echo "Type=${Type} type=${type} size=${size}"

	#
	# Compute the file name.
	#
	file=${Type}Data.java
	fileJvm=${Type}DataJvm.java
	fileCni=${Type}DataCni.java

	#
	# And create files...
	#
	rm -f ${file}
	sed -e "1,45s/Byte/${Type}/g" -e "48s/Byte/${Type}/" -e "49,90s/Byte/${Type}/g" -e "42,44s/byte/${type}/" -e "74s/byte/${type}/" -e "81s/byte/${type}/" -e "89s/byte/${type}/" ByteData.java > ${file}
	sed -e "s/Byte/${Type}/g" -e "14s/byte/${type}/" -e "22,26s/byte/${type}/" -e "45s/byte/${type}/" -e "54s/byte/${type}/" -e "60s/byte/${type}/" ByteDataCni.java > ${fileCni} 
	sed -e "1,28s/Byte/${Type}/g" -e "29s/Byte/${Type}/" -e "32,68s/Byte/${Type}/g" -e "48s/byte/${type}/" -e "57s/byte/${type}/" -e "39s/bb;/b;/" -e "17cByteBuffer b;" -e "24,25cb=ByteBuffer.allocateDirect(count*${size});b.order(ByteOrder.nativeOrder());bb=b.as${Type}Buffer();" -e "31cb=data;bb=b.as${Type}Buffer();" -e "63s/byte/${type}/" ByteDataJvm.java > ${fileJvm} 
done


