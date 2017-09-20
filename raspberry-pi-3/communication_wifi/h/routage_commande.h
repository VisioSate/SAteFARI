

#ifndef ROUTAGE_H
#define ROUTAGE_H


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
#include <sys/time.h>	

#include <poll.h>	

/* Taille maximale du buffer - Pas encore nécéssaire */
#define BUFLEN 512 
/* Port d'entrée de la communication wifi */
#define PORT 12345   

/*struct timeval
{
	time_t tv_sec;
	long int tv_usec;
};
*/
void die(char *s);

#endif
