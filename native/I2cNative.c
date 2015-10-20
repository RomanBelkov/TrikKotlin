#include <fcntl.h>
#include <sys/ioctl.h>
#include <linux/i2c-dev.h>
#include <linux/i2c.h>
#include <unistd.h>

#include <jni.h>

#include "I2cNative.h"

static inline __s32 i2c_smbus_access(int file, char read_write, __u8 command
		, int size, union i2c_smbus_data *data)
{
    struct i2c_smbus_ioctl_data args;

    args.read_write = read_write;
    args.command = command;
    args.size = size;
    args.data = data;
    return ioctl(file, I2C_SMBUS, &args);
}

static inline __s32 i2c_smbus_read_i2c_block_data(int file, __u8 command,
   __u8 length, __u8 *values)
{
	union i2c_smbus_data data;

	if (length > 32) {
		length = 32;
	}

	data.block[0] = length;
	if (i2c_smbus_access(file, I2C_SMBUS_READ, command
			, length == 32 ? I2C_SMBUS_I2C_BLOCK_BROKEN : I2C_SMBUS_I2C_BLOCK_DATA
			, &data))
	{
			return -1;
	} else {
			for (int i = 1; i <= data.block[0]; i++) {
					values[i - 1] = data.block[i];
			}

			return data.block[0];
	}
}

static inline __s32 i2c_smbus_write_byte_data(int file, __u8 command, __u8 value)
{
	union i2c_smbus_data data;
	data.byte = value;
	return i2c_smbus_access(file, I2C_SMBUS_WRITE, command, I2C_SMBUS_BYTE_DATA, &data);
}

static inline __s32 i2c_smbus_write_word_data(int file, __u8 command, __u16 value)
{
	union i2c_smbus_data data;
	data.word = value;
	return i2c_smbus_access(file, I2C_SMBUS_WRITE, command, I2C_SMBUS_WORD_DATA, &data);
}

//JNI part begins here

JNIEXPORT jint JNICALL Java_I2cNative_openBus
  (JNIEnv *env, jclass jcl, jstring busName, jint deviceAddress)
{
    char fileName[256];
		__u8 devAddr = deviceAddress & 0xFF;
	  int len = (*env)->GetStringLength(env, busName);
	  (*env)->GetStringUTFRegion(env, busName, 0, len, fileName);

	  int fd = open(fileName, O_RDWR);
    if (fd < 0 || ioctl(fd, I2C_SLAVE, deviceAddress) < 0 ) {
      return -1;
    } else {
      return fd;
    }
}

JNIEXPORT jint JNICALL Java_I2cNative_closeBus
  (JNIEnv *env, jclass jcl, jint fd)
{
    return close (fd);
}

JNIEXPORT jint JNICALL Java_I2cNative_writeByte
  (JNIEnv *env, jclass jcl, jint fd, jint address, jint b)
{
		__u8 addr = address & 0xFF;
		__u8 byte = b       & 0xFF;
    return i2c_smbus_write_byte_data(fd, addr, byte);
}

JNIEXPORT jint JNICALL Java_I2cNative_writeWord
  (JNIEnv *env, jclass jcl, jint fd, jint address, jint word)
{
		__u8  addr = address & 0xFF;
		__u16 value = word & 0xFFFF;
    return i2c_smbus_write_word_data(fd, addr, value);
}

JNIEXPORT jint JNICALL Java_I2cNative_readWord
  (JNIEnv *env, jclass jcl, jint fd, jint address)
{
  union i2c_smbus_data data;
	__u8  addr = address & 0xFF;
  if (i2c_smbus_access(fd, I2C_SMBUS_READ, addr, I2C_SMBUS_WORD_DATA, &data)) {
    return -1;
  } else {
    return (jint)(0x0FFFF & data.word);
  }
}

JNIEXPORT jlong JNICALL Java_I2cNative_readAllBytes
  (JNIEnv *env, jclass jcl, jint fd, jint address)
{
		__u8 addr = address & 0xFF;
    __u8 buffer[4] = {0};
		i2c_smbus_read_i2c_block_data(fd, addr, 4, buffer);
		return (jlong)(buffer[3] << 24 | buffer[2] <<  16 | buffer[1] << 8 | buffer[0]);
}
