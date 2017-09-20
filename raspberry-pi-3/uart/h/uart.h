#ifndef UART_H
#define UART_H

#include<string.h>
/* Header nécéssaire à l'UART */
#include <stdio.h>
#include <unistd.h>			//Used for UART
#include <fcntl.h>			//Used for UART
#include <termios.h>		//Used for UART



/* Header nécessaire à la communication wifi */

#include<stdio.h> 
#include<string.h> 
#include<stdlib.h> 
#include<arpa/inet.h>
#include<sys/socket.h>


/* Header nécessaire à la communication I2C */
#include <unistd.h>				
#include <fcntl.h>				
#include <sys/ioctl.h>			
#include <linux/i2c-dev.h>		

/* Taille maximale du buffer - Pas encore nécéssaire */
#define BUFLEN 512 
/* Port d'entrée de la communication wifi */
#define PORT 12345   


void die(char *s);
void affichage(int filestream,unsigned char* buffer_to_display,int lenght_buffer);
void receive(int filestream, unsigned char* rx_buffer);
void init_uart(int filestream);
void close_uart(int filestream);
void receive_char(int filestream, unsigned char* rx_buffer);

#endif




