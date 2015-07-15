#include "bbbandroidHAL.h"
#include <stdio.h>

int main()
{
	int fd = spiOpen(1, 0, 10000, 3, 8);

	printf("%d->\n", fd);

	unsigned char value = 0b00000010; 

	unsigned char null = 0x00;
	
	spiTransfer(fd, &value, &null, 1);

	spiClose(fd);
	return 0;
}