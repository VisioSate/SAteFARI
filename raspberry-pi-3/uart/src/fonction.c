#include "uart.h"

	
void affichage(int filestream,unsigned char* buffer_to_display,int lenght_buffer) 
{

	int i;
	unsigned char *p_tx_buffer;
	p_tx_buffer = &buffer_to_display[0];
	
for (i=0;i<lenght_buffer;i++) {
		*p_tx_buffer++;
	}

	if (filestream != -1)
	{
		int count = write(filestream, &buffer_to_display[0], (p_tx_buffer - &buffer_to_display[0]));		//Filestream, bytes to write, number of bytes to write
		if (count < 0)
		{
			printf("UART TX error\n");
		}
	}


}



void receive(int filestream, unsigned char* rx_buffer) 
{

	char c;
	int n;
    int i = 0;
//----- CHECK FOR ANY RX BYTES -----
	if (filestream != -1)
	{
		// Read up to 255 characters from the port if they are there
		do 
		{	
			n = read(filestream, (void*)&c, 1);		
	    	affichage(filestream,&c,1);	
			
			if (n < 0)
			{
				printf("ERROR ERROR ERROR");
			}
			else if (n == 0)
			{
				printf("no data waiting");
			}
			else
			{
				rx_buffer[i] = c;
				i++;	 
			}
		}
		while (c != '\r' && i < 255 && n > 0); 
		rx_buffer[i] = '\0';	
	}

}

void receive_char(int filestream, unsigned char* rx_buffer)
{

	if (filestream != -1)
	{

		int rx_length = read(filestream, (void*)rx_buffer, 255);		
		if (rx_length < 0)
		{
			
		}
		else if (rx_length == 0)
		{
			//No data waiting
		}
		else
		{
			//Bytes received
			rx_buffer[rx_length] = '\0';
			printf("%i bytes read : %s\n", rx_length, rx_buffer);
		}
	}
}




