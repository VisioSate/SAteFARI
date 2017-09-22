#ifndef UART_H
#define UART_H
/**
 *	\file	uart.h
 *	\biref	fonction nécessaire au fonctionnement de l'UART
 *	\authors Irian.J Nicolas.D
 *	\version 1.0
 *	\date 22 Septembre 2017
 *
 */

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

/**
 *	\fn	void affichage (int filestream,unsigned char* buffer_to_display,int lenght_buffer)
 *	\brief Envoie un string sur le port série
 *
 */ 
void affichage(int filestream,unsigned char* buffer_to_display,int lenght_buffer);

/**
 *	\fn	void receive(int filestream, unsigned char* rx_buffer); buffer_to_display,int lenght_buffer)
 *	\brief se met en écoute sur le port série pour recevoir lenght_buffer octets de données
 *
 *
 */ 
void receive(int filestream, unsigned char* rx_buffer);

/**
 *	\fn	void close_uart(int filestream);
 *	\brief Ferme la communication port série 
 *
 */ 
void close_uart(int filestream);

/**
 *	\fn	void receive(int filestream, unsigned char* rx_buffer); buffer_to_display,int lenght_buffer)
 *	\brief se met en écoute sur le port série pour un octet de donnée - Non fonctionnel 
 *
 */ 
void receive_char(int filestream, unsigned char* rx_buffer);

#endif




